package com.wilsonhernandez.credibanco.core.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("authorizationEntity")
data class AuthorizationEntity(
    @PrimaryKey(autoGenerate = true) val id: Int=0,
    val receiptId: String,
    val rrn: String,
    val statusCode: String,
    val statusDescription: String,
    val commerceCode:String,
    val terminalCode: String,
    val state: Boolean=true
)