package com.wilsonhernandez.credibanco.domain

import android.os.Build
import androidx.annotation.RequiresApi
import com.wilsonhernandez.credibanco.data.repository.CancelApiRepository
import com.wilsonhernandez.credibanco.data.network.response.CancelResponse
import javax.inject.Inject

class CancelUseCase @Inject constructor(val repository: CancelApiRepository) {

    @RequiresApi(Build.VERSION_CODES.O)
    suspend operator fun invoke(
        receiptId: String,
        rrn: String,
        commerceCode: String,
        terminalCode: String,
        success: (cancelResponse: CancelResponse) -> Unit,
        failed: () -> Unit
    ) {
        repository.annulation(receiptId, rrn, commerceCode, terminalCode, success, failed)
    }
}
