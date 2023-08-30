package com.wilsonhernandez.credibanco.authorization.data

import android.os.Build
import androidx.annotation.RequiresApi
import com.wilsonhernandez.credibanco.authorization.data.network.AuthorizationService
import com.wilsonhernandez.credibanco.authorization.data.network.response.AuthorizationResponse

class AuthorizationApiRepository {
   private val api = AuthorizationService()

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