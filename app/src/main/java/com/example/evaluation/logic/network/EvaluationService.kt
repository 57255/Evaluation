package com.example.evaluation.logic.network

import com.example.evaluation.logic.*
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

interface EvaluationService {
    @POST("/employee/login")
    fun login(@Body login: Login): Call<LoginResponse>
    @GET("/performance/getEligibleEmployees")
    fun getEligibleEmployees(@Header("token") token:String,@Query("PerformanceRecordID") performanceRecordId: Int): Call<GetExamList>
    @GET("/employee/info")
    fun getInfo(@Header("token") token:String,@Query("username") username:String): Call<GetEmployeeInformation>

    @POST("/employee/changepassword")
    fun changePassword(@Header("token") token: String,@Query("password") password:String,@Query("newPassword") newPassword:String): Call<LoginResponse>

    @DELETE("/empolyee/logout")
    fun logout(@Header("token") token: String): Call<LoginResponse>

    @POST("/employee/post")
    fun postEvaluation(@Query("recordId") recordId:Int,@Header("token") token: String, @Body postEvaluationResult: PostEvaluationResult): Call<LoginResponse>

    @GET("/evaluation/get")
    fun getEvaluationTitle(@Header("token") token: String,@Query("category") category:String): Call<GetEvaluationTitleResponse>

    @GET("/employee/getSurperior")
    fun getSurperior(@Header("token") token: String,@Query("recordId") recordId: Int): Call<GetSuperiorResponse>

    @GET("/employee/getSubordinates")
    fun getSubordinates(@Header("token") token: String,@Query("recordId") recordId: Int): Call<GetSuperiorResponse>

    @GET("/employee/getSameLevel")
    fun getSameLevel(@Header("token") token: String,@Query("recordId") recordId: Int): Call<GetSuperiorResponse>

    @Multipart
    @POST("/employee/setAvater")
    fun setAvater(@Header("token") token:String,@Part avater: MultipartBody.Part):Call<LoginResponse>

    @GET("/employee/getAvater")
    fun getAvater(@Header("token") token:String):Call<GetAvaterResponse>

    @GET("/employee/getHomeCompanyImage")
    fun getHomeCompanyImage(@Header("token") token:String):Call<GetEmployeeAvaterResponse>

    @GET("/employee/getHomeAnnouncement")
    fun getHomeAnnouncement(@Header("token") token:String):Call<GetAvaterResponse>

    @GET("/employee/getHomeEmployeePicture")
    fun getHomeEmployeePicture(@Header("token") token:String):Call<GetEmployeeAvaterResponse>

    @POST("/sms/send")
    fun sendSMS(@Header("token") token:String,@Query("phone") phone:String): Call<LoginResponse>

    @POST("/sms/verify-code")
    fun verifyCode(@Header("token") token:String,@Query("phone") phone:String,@Query("code") code:String): Call<LoginResponse>


    @GET("/employee/getLeaderboardData")
    fun getRank(@Header("token") token:String,@Query("recordId") recordId: Int): Call<GetRankResponse>

    @POST("/employee/updateRank")
    fun updateRank(@Header("token") token:String,@Query("rank") rank:String): Call<LoginResponse>

    @GET("/employee/getEvaluationInfo")
    fun getEvaluationInfo(@Header("token") token:String): Call<GetEvaluationInfoResponse>

    @GET("/performance/getPerformanceRecord")
    fun getPerformanceRecord(@Header("token") token:String): Call<GetPerformanceRecordResponse>

    @POST("/performance/commitPerformance")
    fun getCommitPerformance(@Header("token") token:String,@Query("AssessesNumber") AssessesNumber:String,@Query("PerformanceRecordID") PerformanceRecordID:Int,@Body performanceMap: PerformanceMap): Call<LoginResponse>

    @GET("/employee/getFinalScore")
    fun getFinalScore(@Header("token") token:String,@Query("recordId") recordId:Int): Call<GetScoreResponse>

    @Multipart
    @POST("/performance/addBonusItem")
    fun addBonusItem(@Header("token") token:String,@Query("PerformanceRecordId") performanceRecordId:Int,@Query("Description") description:String,@Query("Score") score:Int,@Query("EmployeeNumber") employeeNumber:String,@Part file: MultipartBody.Part): Call<LoginResponse>

    @GET("/employee/getEvaluationRecord")
    fun getAllEvaluationRecord(@Header("token") token:String,@Query("recordId") recordId: Int): Call<GetEvaluationRecordResponse>

    @GET("/performance/getPerformanceScore")
    fun getPerformanceScore(@Header("token") token:String,@Query("PerformanceRecordID") recordId: Int): Call<GetScoreResponse>

    @GET("/performance/getAssessorTotalScore")
    fun getAssessorPerformanceDetail(@Header("token") token:String,@Query("performaceRecordId") recordId: Int): Call<GetPerformanceTotalScore>

    @POST("/employee/setCompletionStatus")
    fun setCompletionStatus(@Header("token") token:String): Call<LoginResponse>

    @POST("/employee/setPerformanceCompletionStatus")
    fun setPerformanceCompletionStatus(@Header("token") token:String): Call<LoginResponse>

    @POST("/performance/completePerformance")
    fun completePerformance(@Header("token") token:String): Call<LoginResponse>

    fun completeDown(@Header("token") token:String): Call<LoginResponse>

    fun completeUp(@Header("token") token:String): Call<LoginResponse>

    fun completeSame(@Header("token") token:String): Call<LoginResponse>

    @POST("/employee/completeEvaluation")
    fun completeEvaluation(@Header("token") token:String,@Query("recordId") recordId: Int): Call<LoginResponse>

    @GET("/performance/getPerformanceQuestions")
    fun getPerformanceQuestion(@Header("token") token:String): Call<GetPerformanceQuestionResponse>

    @GET("/performance/getAllPerformanceScore")
    fun getAllPerformanceScore(@Header("token") token:String,@Query("PerformanceRecordID") performanceRecordId:Int): Call<GetAllPerformanceScoreResponse>

    @GET("/evaluation/getAllPosition")
    fun getAllPosition(@Header("token") token:String): Call<GetAllPositionResponse>

    @POST("/employee/bindPhone")
    fun bindPhone(@Header("token") token:String,@Query("phone") phone:String): Call<LoginResponse>
    @POST("/employee/changePhone")
    fun changePhone(@Header("token") token:String,@Query("phone") phone:String,@Query("password") password: String): Call<LoginResponse>


    @GET("/VersionUpdate/check")
    fun check(@Header("token") token:String,@Query("currentVersion") ver:String):Call<VersionResponse>

}