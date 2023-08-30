package com.wilsonhernandez.credibanco.authorization.data.network

import android.os.Build
import androidx.annotation.RequiresApi
import com.wilsonhernandez.credibanco.authorization.data.network.request.AuthorizationRequest
import com.wilsonhernandez.credibanco.authorization.data.network.response.AuthorizationResponse
import com.wilsonhernandez.credibanco.core.network.RetrofitHelper
import com.wilsonhernandez.credibanco.ui.util.convertBase64
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Response
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.await
import retrofit2.awaitResponse

class AuthorizationService {
    private val retrofit = RetrofitHelper.getRetrofit()

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun authorization(
        id: String,
        commerceCode: String,
        terminalCode: String,
        amount: String,
        card: String,
        success: (authorizationResponse: AuthorizationResponse) -> Unit,
        failed: () -> Unit
    ) {
        val commerceBase64 = convertBase64(commerceCode)
        val terminalBase64 = convertBase64(terminalCode)
        val apiService = retrofit.create(AuthorizationClient::class.java)
        val auth = "Basic $commerceBase64$terminalBase64"

        val authenticatorRequest = AuthorizationRequest(
            id = id,
            commerceCode = commerceCode,
            terminalCode = terminalCode,
            amount = amount,
            card = card
        )
        withContext(Dispatchers.IO){


            try {
                val response= apiService.authorization(auth = auth,authenticatorRequest).execute()
                if (response.isSuccessful) {
                    response.body()?.let { success.invoke(it) }
                } else {
                    failed.invoke()
                }
            } catch (e: HttpException) {
                failed.invoke()
            }
        }


    }
}
