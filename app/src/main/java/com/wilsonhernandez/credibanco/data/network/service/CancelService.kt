package com.wilsonhernandez.credibanco.data.network.service

import android.os.Build
import androidx.annotation.RequiresApi
import com.wilsonhernandez.credibanco.data.network.AuthorizationClient
import com.wilsonhernandez.credibanco.data.network.request.CancelRequest
import com.wilsonhernandez.credibanco.data.network.response.CancelResponse
import com.wilsonhernandez.credibanco.core.RetrofitHelper
import com.wilsonhernandez.credibanco.util.convertBase64
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response

class CancelService {
    private val retrofit = RetrofitHelper.getRetrofit()
    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun annulation(
        receiptId: String,
        rrn: String,
        commerceCode: String,
        terminalCode: String,
        success: (cancelResponse: CancelResponse) -> Unit,
        failed: () -> Unit
    ){
        val commerceBase64 = convertBase64(commerceCode)
        val terminalBase64 = convertBase64(terminalCode)
        val apiService = retrofit.create(AuthorizationClient::class.java)
        val auth = "Basic $commerceBase64$terminalBase64"

        val cancelRequest = CancelRequest(receiptId = receiptId,rrn = rrn)
        withContext(Dispatchers.IO){


            apiService.annulation(auth = auth,cancelRequest).enqueue(object :Callback<CancelResponse>{
                override fun onResponse(
                    call: Call<CancelResponse>,
                    response: Response<CancelResponse>
                ) {
                    if (response.body()!=null){
                        success.invoke(response.body()!!)
                    }else{
                        failed.invoke()
                    }
                }

                override fun onFailure(call: Call<CancelResponse>, t: Throwable) {
                    failed.invoke()
                }

            })
        }
    }
}