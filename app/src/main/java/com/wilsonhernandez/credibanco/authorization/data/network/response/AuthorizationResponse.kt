package com.wilsonhernandez.credibanco.authorization.data.network.response

import com.google.gson.annotations.SerializedName
import java.io.Serializable


data class AuthorizationResponse(
    @SerializedName("receiptId") val receiptId: String,
    @SerializedName("rrn") val rrn: String,
    @SerializedName("statusCode") val statusCode: String,
    @SerializedName("statusDescription") val statusDescription: String,
):Serializable