package com.example.evaluation.logic.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Service2 {
    private const val BASE_URL = "http://110.41.60.28:68"
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    fun <T> create(serviceClass: Class<T>): T = retrofit.create(serviceClass)
    inline fun <reified T> create(): T = create(T::class.java)
}