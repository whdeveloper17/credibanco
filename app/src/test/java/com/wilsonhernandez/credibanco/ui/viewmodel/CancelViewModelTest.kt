package com.wilsonhernandez.credibanco.ui.viewmodel


import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.wilsonhernandez.credibanco.data.database.entities.TransactionsEntity
import com.wilsonhernandez.credibanco.data.network.response.CancelResponse
import com.wilsonhernandez.credibanco.data.repository.DatabaseRepository
import com.wilsonhernandez.credibanco.domain.CancelUseCase
import com.wilsonhernandez.credibanco.util.NetworkConnectivity
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.*

import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class CancelViewModelTest {
    @RelaxedMockK
    private lateinit var databaseRepository: DatabaseRepository

    @RelaxedMockK
    private lateinit var cancelUseCase :CancelUseCase

    private lateinit var cancelViewModel: CancelViewModel

    @RelaxedMockK
    private lateinit var networkConnectivity: NetworkConnectivity

    @get:Rule
    var rule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        cancelViewModel= CancelViewModel(databaseRepository,cancelUseCase,networkConnectivity)
        Dispatchers.setMain(Dispatchers.Unconfined)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }


    @Test
    fun `when getListStatusApproved is called, it should update listAuthorization correctly`() = runTest {
        val transactionsEntity = TransactionsEntity(0,"","","","","","",true)
        val mockTransactionList = listOf(transactionsEntity)
        coEvery { databaseRepository.getListTransactionsApproved() } returns flowOf(mockTransactionList)

        cancelViewModel.getListStatusApproved()

        assertEquals(mockTransactionList, cancelViewModel.listAuthorization.value)
    }

    @Test
    fun `when onCancelAuthorization is called with success, it should update isSnackbar correctly`() = runTest {
        val mockTransaction =TransactionsEntity(0,"","","","","","",true)

        val successResponse = CancelResponse("00","")

        coEvery { networkConnectivity.checkNetworkConnectivity() } returns true
            coEvery { cancelUseCase(any(), any(), any(), any(), any(), any()) } coAnswers {
                val successCallback = arg<(CancelResponse) -> Unit>(4)
                successCallback(successResponse)
            }


        cancelViewModel.onCancelAuthorization(mockTransaction)

        assertEquals("Anulacion  exitosa", cancelViewModel.isSnackbar.value)
    }

    @Test
    fun `when onCancelAuthorization is called with error, it should update isSnackbar correctly`() = runTest {

        val mockTransaction =TransactionsEntity(0,"","","","","","",true)
        coEvery { networkConnectivity.checkNetworkConnectivity() } returns true
        coEvery { cancelUseCase(any(), any(), any(), any(), any(), any()) } coAnswers {
                val errorCallback = arg<() -> Unit>(5)
                errorCallback()
            }

        cancelViewModel.onCancelAuthorization(mockTransaction)

        assertEquals("Anulacion erronea", cancelViewModel.isSnackbar.value)
    }


}