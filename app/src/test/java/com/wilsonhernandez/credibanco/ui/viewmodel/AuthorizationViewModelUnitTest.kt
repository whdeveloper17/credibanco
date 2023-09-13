package com.wilsonhernandez.credibanco.ui.viewmodel

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.wilsonhernandez.credibanco.data.network.response.AuthorizationResponse
import com.wilsonhernandez.credibanco.data.repository.DatabaseRepository
import com.wilsonhernandez.credibanco.domain.AuthorizationUseCase
import com.wilsonhernandez.credibanco.settings.SettingsUtilImpl
import com.wilsonhernandez.credibanco.util.NetworkConnectivity
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertNull
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class AuthorizationViewModelUnitTest {

    @RelaxedMockK
    private lateinit var getAuthorizationUseCase: AuthorizationUseCase

    @RelaxedMockK
    private lateinit var databaseRepository: DatabaseRepository

    @RelaxedMockK
    private lateinit var context: Context
    private lateinit var authorizationViewModel: AuthorizationViewModel

    @RelaxedMockK
    private lateinit var settingsUtilImpl: SettingsUtilImpl
    @RelaxedMockK
    private lateinit var networkConnectivity: NetworkConnectivity

    @get:Rule
    var rule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        authorizationViewModel =
            AuthorizationViewModel(
                context,
                databaseRepository,
                getAuthorizationUseCase,
                settingsUtilImpl,
                networkConnectivity
            )
        Dispatchers.setMain(Dispatchers.Unconfined)
    }

    @After
    fun onAfter() {
        Dispatchers.resetMain()
    }

    @Test
    fun `when all the fields have data and the generate authorization button is enabled`() =
        runTest {
            val id = "123123"
            val commerceCode = "000123"
            val terminalCode = "000ABC"
            val amount = "12345"
            val card = "1234567789121212"

            coEvery { settingsUtilImpl.getAndroidId(context) } returns "12344567"


            authorizationViewModel.onAuthorizationChanged(
                id = id,
                commerceCode = commerceCode,
                terminalCode = terminalCode,
                amount = amount,
                card = card
            )

            assertEquals(commerceCode, authorizationViewModel.commerceCode.value)
            assertEquals(terminalCode, authorizationViewModel.terminalCode.value)
            assertEquals(amount, authorizationViewModel.amount.value)
            assertEquals(card, authorizationViewModel.card.value)
            assertTrue(authorizationViewModel.isAuthorizationEnable.value!!)
        }

    @Test
    fun `when any of the fields is empty or invalid, the authorization button should be disabled`() =
        runTest {

            val id = ""
            val commerceCode = "000123"
            val terminalCode = ""
            val amount = "12345"
            val card = "12345"

            val isAuthorizationEnabled = authorizationViewModel.enableAuthorization(
                id,
                commerceCode,
                terminalCode,
                amount,
                card
            )
            assertFalse(isAuthorizationEnabled)
        }

    @Test
    fun `when authorization is successful, it should update state correctly`() = runTest {
        coEvery { networkConnectivity.checkNetworkConnectivity() } returns true
        coEvery {
            getAuthorizationUseCase(
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any()
            )
        } coAnswers {
            val successCallback = args[5] as (AuthorizationResponse) -> Unit
            successCallback.invoke(AuthorizationResponse("1321312312312", "324234234234", "00", "statusDescription"))
        }
            authorizationViewModel.onAuthorization(
                "121312",
                "000123",
                "000ABC",
                "123123",
                "1234567890123456"
            )


        assertFalse(authorizationViewModel.isAuthorizationEnable.value!!)
        assertFalse(authorizationViewModel.isLoading.value!!)
        assertTrue(authorizationViewModel.succesAuthorization.value!!)
        assertNull(authorizationViewModel.isSnackbar.value)
    }

    @Test
    fun `when authorization fails, it should update state correctly`() = runTest {
        coEvery { networkConnectivity.checkNetworkConnectivity() } returns true

        coEvery {
            getAuthorizationUseCase(
                any(),
                any(),
                any(),
                any(),
                any(),
                any(),
                any()
            )
        } coAnswers {
            val failedCallback = args[6] as () -> Unit
            failedCallback()
        }
        authorizationViewModel.onAuthorization("asdas", "asdas", "asdas", "adsas", "")



        assertTrue(authorizationViewModel.isAuthorizationEnable.value!!)


        assertFalse(authorizationViewModel.isLoading.value!!)


        assertFalse(authorizationViewModel.succesAuthorization.value!!)


        assertEquals("Autorización erronea", authorizationViewModel.isSnackbar.value)
    }
}


