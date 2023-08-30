package com.wilsonhernandez.credibanco.cancel.domain

import android.os.Build
import androidx.annotation.RequiresApi
import com.wilsonhernandez.credibanco.cancel.data.network.CancelApiRepository
import com.wilsonhernandez.credibanco.cancel.data.network.request.CancelRequest
import com.wilsonhernandez.credibanco.cancel.data.network.response.CancelResponse

class CancelUseCase {
    val repository = CancelApiRepository()

    @RequiresApi(Build.VERSION_CODES.O)
    suspend operator fun invoke(
        receiptId: String,
        rrn: String,
        commerceCode: String,
        terminalCode: String,
        success: (cancelResponse: CancelResponse) -> Unit,
        failed: () -> Unit
    ) {
        repository.annulation(receiptId,rrn,commerceCode, terminalCode, success, failed)
    }
}