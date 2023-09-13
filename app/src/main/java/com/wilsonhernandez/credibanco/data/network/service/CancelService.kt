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
import retrofit2.HttpException

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


            try {
                val response= apiService.annulation(auth = auth,cancelRequest).execute()
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