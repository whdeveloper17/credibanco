package com.wilsonhernandez.credibanco.data.network.service

import android.os.Build
import androidx.annotation.RequiresApi
import com.wilsonhernandez.credibanco.data.network.request.AuthorizationRequest
import com.wilsonhernandez.credibanco.data.network.response.AuthorizationResponse
import com.wilsonhernandez.credibanco.core.RetrofitHelper
import com.wilsonhernandez.credibanco.data.network.AuthorizationClient
import com.wilsonhernandez.credibanco.util.convertBase64
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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
        withContext(Dispatchers.IO) {


            /*try {
                val response = apiService.authorization(auth = auth, authenticatorRequest).execute()
                if (response.isSuccessful) {
                    response.body()?.let { success.invoke(it) }
                } else {
                    failed.invoke()
                }
            } catch (e: HttpException) {
                failed.invoke()
            }*/

            apiService.authorization(auth = auth, authenticatorRequest).enqueue(object :
                Callback<AuthorizationResponse> {
                override fun onResponse(
                    call: Call<AuthorizationResponse>,
                    response: Response<AuthorizationResponse>
                ) {
                    if (response.body()!=null){
                        success.invoke(response.body()!!)
                    }else{
                        failed.invoke()
                    }
                }

                override fun onFailure(call: Call<AuthorizationResponse>, t: Throwable) {
                    failed.invoke()
                }


            })
        }


    }
}
