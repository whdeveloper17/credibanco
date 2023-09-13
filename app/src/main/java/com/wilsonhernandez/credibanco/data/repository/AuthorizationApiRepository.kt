package com.wilsonhernandez.credibanco.data.repository

import android.os.Build
import androidx.annotation.RequiresApi
import com.wilsonhernandez.credibanco.data.network.service.AuthorizationService
import com.wilsonhernandez.credibanco.data.network.response.AuthorizationResponse
import javax.inject.Inject

class AuthorizationApiRepository @Inject constructor(private val api : AuthorizationService) {

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
         api.authorization(id,commerceCode, terminalCode,amount,card,success,failed)
    }
}