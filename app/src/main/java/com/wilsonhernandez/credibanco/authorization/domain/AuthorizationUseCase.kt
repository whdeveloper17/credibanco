package com.wilsonhernandez.credibanco.authorization.domain

import android.os.Build
import androidx.annotation.RequiresApi
import com.wilsonhernandez.credibanco.authorization.data.AuthorizationRepository
import com.wilsonhernandez.credibanco.authorization.data.network.response.AuthorizationResponse

class AuthorizationUseCase {
    val repository = AuthorizationRepository()

    @RequiresApi(Build.VERSION_CODES.O)
    suspend operator fun invoke(
        id: String,
        commerceCode: String,
        terminalCode: String,
        amount: String,
        card: String,
        success: (authorizationResponse: AuthorizationResponse) -> Unit,
        failed: () -> Unit
    ) {
        repository.authorization(id,commerceCode, terminalCode,amount,card, success, failed)
    }
}