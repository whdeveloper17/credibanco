package com.wilsonhernandez.credibanco.ui.util

import android.os.Build
import androidx.annotation.RequiresApi
import java.util.Base64


fun insertDecimal(number: Int): String {

        val numberString = number.toString()
        return if (numberString.length >3 && numberString.length <=4){
            val decimalString = StringBuilder(numberString).apply {
                insert(length - 1, '.')
            }.toString()

            decimalString
        }else if(numberString.length>4){
            val decimalString = StringBuilder(numberString).apply {
                insert(length - 2, '.')
            }.toString()

            decimalString
        }  else{
            numberString
        }
    }

@RequiresApi(Build.VERSION_CODES.O)
fun convertBase64(value:String):String{
    val encoder: Base64.Encoder = Base64.getEncoder()
    return encoder.encodeToString(value.toByteArray())
}
