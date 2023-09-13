package com.wilsonhernandez.credibanco.data.network.response

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class CancelResponse(
    @SerializedName("statusCode") val statusCode: String,
    @SerializedName("statusDescription") val statusDescription: String,
) : Serializable