package com.example.evaluation.logic.network

import com.example.evaluation.logic.GetEvaluationTitleResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface service {
    @GET("/evaluation/getQzhVersion")
    fun getTitle3(@Query("level") level:String,@Header("token") token:String): Call<GetEvaluationTitleResponse>
}