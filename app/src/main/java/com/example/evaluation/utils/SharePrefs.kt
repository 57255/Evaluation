package com.example.evaluation.utils

import android.content.Context

class SharePrefs {
    fun getToken(context: Context, key:String): String? {
        val prefs=context.getSharedPreferences("data", Context.MODE_PRIVATE)
        return prefs.getString(key,toString())
    }
}