package com.wilsonhernandez.credibanco.cancel.data.network

import android.os.Build
import androidx.annotation.RequiresApi
import com.wilsonhernandez.credibanco.authorization.data.network.AuthorizationService
import com.wilsonhernandez.credibanco.authorization.data.network.response.AuthorizationResponse
import com.wilsonhernandez.credibanco.cancel.data.network.response.CancelResponse

class CancelApiRepository {
   private val api = CancelService()

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