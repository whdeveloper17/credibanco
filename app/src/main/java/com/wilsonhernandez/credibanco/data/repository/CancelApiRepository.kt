package com.wilsonhernandez.credibanco.data.repository

import android.os.Build
import androidx.annotation.RequiresApi
import com.wilsonhernandez.credibanco.data.network.service.CancelService
import com.wilsonhernandez.credibanco.data.network.response.CancelResponse
import javax.inject.Inject

class CancelApiRepository @Inject constructor(private val api:CancelService){

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun annulation(
        receiptId: String,
        rrn: String,
        commerceCode: String,
        terminalCode: String,
        success: (cancelResponse: CancelResponse) -> Unit,
        failed: () -> Unit
    ) {
         api.annulation(receiptId,rrn,commerceCode, terminalCode,success,failed)
    }
}