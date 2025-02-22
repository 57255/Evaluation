package com.example.evaluation.utils

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context

class MyApplication : Application() {

    @SuppressLint("StaticFieldLeak")
    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
    }
    override fun onCreate() {
        super.onCreate()
        context = applicationContext
    }
}