package com.wilsonhernandez.credibanco.settings

import android.content.Context
import android.provider.Settings

class SettingsUtilImpl : SettingsUtil {
    override fun getAndroidId(context: Context): String {
        return Settings.Secure.getString(context.contentResolver, android.provider.Settings.Secure.ANDROID_ID)
    }

}