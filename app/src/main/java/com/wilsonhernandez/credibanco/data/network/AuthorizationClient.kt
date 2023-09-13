package com.wilsonhernandez.credibanco.data.network

import com.wilsonhernandez.credibanco.data.network.request.AuthorizationRequest
import com.wilsonhernandez.credibanco.data.network.response.AuthorizationResponse
import com.wilsonhernandez.credibanco.data.network.request.CancelRequest
import com.wilsonhernandez.credibanco.data.network.response.CancelResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface AuthorizationClient {
    @POST("authorization/")
    fun authorization(@Header("Authorization") auth:String,@Body() data : AuthorizationRequest):Call<AuthorizationResponse>

    @POST("annulment/")
    fun annulation(@Header("Authorization") auth:String,@Body() data : CancelRequest):Call<CancelResponse>
}