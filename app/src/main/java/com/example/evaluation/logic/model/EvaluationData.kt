package com.example.evaluation.logic



import java.io.File

data class Evaluation(var avatar:String, var fullName:String, val finalScore:Int)
data class Login(val username:String,val password:String)
data class LoginResponse(val code:Int,val message:String,val data:String)
data class GetScoreResponse(val code:Int,val message:String,val data:Int)
data class Employee(
    val avatar:String,
    val complete: Boolean,
    val department: String,
    val employeeId: Int,
    val fullName: String,
    val password: String,
    val performanceComplete: Boolean,
    val phone: String,
    val position: String,
    val rank1:String,
    val representative: Boolean,
    val username: String,
    var evaluated:Boolean=false
)
data class GetExamList(
    val code: Int,
    val data: ArrayList<ExamData>,
    val message: String
)
data class ExamData(
    val evaluatee: String,
    val evaluateeName: String,
    val evaluator: String,
    val scoreRelationId: Int,
    var evaluated:Boolean=false
)
data class GetEmployeeInformation(
    val code: Int,
    val data: Employee,
    val message: String
)
data class PostEvaluationResult(val questionScorePairs: List<QuestionScorePairs>)
data class QuestionScorePairs(
    val questionId: Int,
    var score: Int,
    val category: String,
    val assessorNumber: String,
    val assessesNumber: String
)
data class GetSuperiorResponse(
    val code: Int,
    val data: ArrayList<Employee>,
    val message: String
)
data class GetAvaterResponse(
    val code: Int,
    val message: String,
    val data: String
)
data class GetEmployeeAvaterResponse(
    val code: Int,
    val message: String,
    val data: List<String>
)
data class GetEvaluationTitleResponse(
    val code: Int,
    val message: String,
    val data: ArrayList<EvaluationTitle>
)
data class EvaluationTitle(
    val category:String,
    val state:Int,
    val order1:Int,
    val evaluationQuestion:String,
    val evaluationId:Int,
    val evaluationScore:Int
)
data class GetRankResponse(
    val code: Int,
    val message: String,
    val data: ArrayList<Evaluation>
)
data class GetEvaluationInfoResponse(
    val code: Int,
    val data: List<EvaluationData>,
    val message: String
)
data class EvaluationData(
    val endTime: String,
    val recordId: Int,
    val startTime: String
)
data class GetPerformanceRecordResponse(
    val code: Int,
    val data: ArrayList<PerformanceRecord>,
    val message: String
)
data class PerformanceRecord (
    val assessesNumber: String,
    val assessorNumber: String,
    val endTime: String,
    val month: Int,
    val performanceRecordId: Int,
    val positionCompetencyScore: Int,
    val startTime: String,
    val time: String,
    val year: Int
)
data class PerformanceMap(
    val performanceMap:ArrayList<Map<String,Int>>
)
data class GetEvaluationRecordResponse(
    val code: Int,
    val data: List<RecordData>,
    val message: String
)
data class RecordData(
    val assessesNumber: String,
    val assessorNumber: String,
    val ethicScore: Int,
    val positionCompetencyScore: Int,
    val recordId: Int,
    val time: String,
    val totalScore: Int
)
data class GetPerformanceQuestionResponse(
    val code: Int,
    val data: List<PerformanceData>,
    val message: String
)
data class PerformanceData(
    val question: String,
    val questionId: Int,
    val score: Int,
    val theme: String
)
data class Question(
    val question:String,
    val score:Int
)
data class GetPerformanceTotalScore(
    val code: Int,
    val data: List<TotalData>,
    val message: String
)
data class TotalData(
    val assessesNumber: String,
    val assessorNumber: String,
    val id: Int,
    val month: Int,
    val performanceRecordId: Int,
    val positionCompetencyScore: Int,
    val time: String,
    val year: Int
)
data class GetAllPerformanceScoreResponse(
    val code: Int,
    val data: ArrayList<AllPerformanceData>,
    val message: String
)
data class AllPerformanceData(
    val addScore: Int,
    val finalScore: Int,
    var fullName: String,
    val performanceFinalScoreId: Int,
    val performanceRecordId: Int,
    val username: String
)
data class GetAllPositionResponse(
    val code: Int,
    val data: List<String>,
    val message: String
)
data class VersionResponse(
    val code: Int,
    val data: VersionData,
    val message: String
)
data class VersionData(
    val changelog: String,
    val latestVersion: String,
    val mandatory: Boolean,
    val updateUrl: String
)
data class PerformanceNewData(
    val evaluatee: String,
    val evaluateeName: String,
    val evaluator: String,
    val evaluatorName: String,
    val performanceDetailId: Int,
    val performanceItem: String,
    val performanceRecordId: Int,
    val performanceScore: Int
)

data class PerformanceNewRecord(
    val code: Int,
    val data: List<PerformanceNewData>,
    val message: String
)