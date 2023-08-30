package com.wilsonhernandez.credibanco.authorization.data.network

import com.wilsonhernandez.credibanco.authorization.data.network.request.AuthorizationRequest
import com.wilsonhernandez.credibanco.authorization.data.network.response.AuthorizationResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface AuthorizationClient {
    @POST("authorization/")
    fun authorization(@Header("Authorization") auth:String,@Body() data :AuthorizationRequest ):Call<AuthorizationResponse>
}