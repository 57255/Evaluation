package com.example.evaluation.logic.network

import com.example.evaluation.logic.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

object EvaluationNetwork {
    private val evaluationService = ServiceCreator.create<EvaluationService>()
    suspend fun login(login: Login) = evaluationService.login(login).await()
    suspend fun getInfo(token:String,username:String)= evaluationService.getInfo(token,username).await()
    suspend fun changePassword(token:String,password: String,newPassword:String)= evaluationService.changePassword(token,password,newPassword).await()
    suspend fun logout(token:String)= evaluationService.logout(token).await()
    suspend fun postEvaluation(recordId:Int,token:String,postEvaluationResult: PostEvaluationResult)= evaluationService.postEvaluation(recordId,token,postEvaluationResult).await()
    suspend fun getEvaluationTitle(token:String,category:String)= evaluationService.getEvaluationTitle(token,category).await()
    suspend fun getSurperior(token:String,recordId: Int)= evaluationService.getSurperior(token,recordId).await()
    suspend fun getSubordinates(token:String,recordId: Int)= evaluationService.getSubordinates(token,recordId).await()
    suspend fun getSameLevel(token:String,recordId: Int)= evaluationService.getSameLevel(token,recordId).await()
    suspend fun sendSMS(token:String,phone:String)= evaluationService.sendSMS(token,phone).await()
    suspend fun verifyCode(token:String,phone:String,code:String)= evaluationService.verifyCode(token,phone,code).await()
    suspend fun setAvater(token: String,avater: File): LoginResponse {
        val requestFile: RequestBody =
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(),avater)
        val body: MultipartBody.Part=
            MultipartBody.Part.createFormData("avater",avater.name,requestFile)
        return evaluationService.setAvater(token,body).await()
    }
    suspend fun getAvater(token:String)= evaluationService.getAvater(token).await()
    suspend fun getHomeCompanyImage(token:String)= evaluationService.getHomeCompanyImage(token).await()
    suspend fun getHomeAnnouncement(token:String)= evaluationService.getHomeAnnouncement(token).await()
    suspend fun getHomeEmployeePicture(token:String)= evaluationService.getHomeEmployeePicture(token).await()
    suspend fun getRank(token:String,recordId: Int)= evaluationService.getRank(token,recordId).await()
    suspend fun updateRank(token:String,rank:String)= evaluationService.updateRank(token,rank).await()
    suspend fun getEvaluationInfo(token:String)= evaluationService.getEvaluationInfo(token).await()
    suspend fun getPerformanceRecord(token:String)= evaluationService.getPerformanceRecord(token).await()
    suspend fun getEmployees(token:String,performanceRecordId: Int)= evaluationService.getEligibleEmployees(token,performanceRecordId).await()
    suspend fun postExam(token:String,assessesNumber:String,performanceRecordId:Int,performanceMap: PerformanceMap)=
        evaluationService.getCommitPerformance(token,assessesNumber,performanceRecordId,performanceMap).await()
    suspend fun getFinalScore(token:String,recordId: Int)= evaluationService.getFinalScore(token,recordId).await()
    suspend fun addBonusItem(token:String,performanceRecordId: Int,description:String,score:Int,employeeNumber:String,proof:File):LoginResponse{
        val requestFile: RequestBody =
            RequestBody.create("multipart/form-data".toMediaTypeOrNull(),proof)
        val body: MultipartBody.Part=
            MultipartBody.Part.createFormData("file",proof.name,requestFile)
        return evaluationService.addBonusItem(token,performanceRecordId,description,score,employeeNumber,body).await()
    }
    suspend fun getAllEvaluationRecord(token:String,recordId:Int)= evaluationService.getAllEvaluationRecord(token,recordId).await()
    suspend fun getPerformanceId(token:String,performanceRecordId: Int)= evaluationService.getPerformanceScore(token,performanceRecordId).await()
    suspend fun getPerformanceRecordDetail(token:String,performanceRecordId: Int)= evaluationService.getAssessorPerformanceDetail(token,performanceRecordId).await()
    suspend fun setCompletionStatus(token:String)= evaluationService.setCompletionStatus(token).await()
    suspend fun setPerformanceCompletionStatus(token:String)= evaluationService.setPerformanceCompletionStatus(token).await()
    suspend fun completePerformance(token:String)= evaluationService.completePerformance(token).await()
    suspend fun completeUp(token:String)= evaluationService.completeUp(token).await()
    suspend fun completeDown(token:String)= evaluationService.completeDown(token).await()
    suspend fun completeSame(token:String)= evaluationService.completeSame(token).await()
    suspend fun completeEvaluation(token:String,recordId: Int)= evaluationService.completeEvaluation(token,recordId).await()
    suspend fun getPerformanceQuestion(token:String)= evaluationService.getPerformanceQuestion(token).await()
    suspend fun getAllPerformanceRecord(token:String,performanceRecordId: Int)= evaluationService.getAllPerformanceScore(token,performanceRecordId).await()
    suspend fun getAllPosition(token:String)= evaluationService.getAllPosition(token).await()
    suspend fun bindPhone(token:String,phone:String)= evaluationService.bindPhone(token,phone).await()
    suspend fun changePhone(token:String,phone:String,password: String)= evaluationService.changePhone(token,phone,password).await()
    suspend fun check(token:String,ver:String)= evaluationService.check(token,ver).await()
    private suspend fun <T> Call<T>.await(): T {
        return suspendCoroutine { continuation ->
            enqueue(object : Callback<T> {
                override fun onResponse(call: Call<T>, response: Response<T>) {
                    val body = response.body()
                    if (body != null) continuation.resume(body)
                    else continuation.resumeWithException(
                        RuntimeException("response body is null"))
                }
                override fun onFailure(call: Call<T>, t: Throwable) {
                    continuation.resumeWithException(t)
                }
            })
        }
    }
}