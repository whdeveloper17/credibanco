package com.wilsonhernandez.credibanco.domain

import android.os.Build
import androidx.annotation.RequiresApi
import com.wilsonhernandez.credibanco.data.repository.AuthorizationApiRepository
import com.wilsonhernandez.credibanco.data.network.response.AuthorizationResponse
import javax.inject.Inject

class AuthorizationUseCase @Inject constructor (private val repository : AuthorizationApiRepository){
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