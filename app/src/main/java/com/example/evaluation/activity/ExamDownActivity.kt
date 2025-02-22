package com.example.evaluation.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.evaluation.adapter.ExamTitleAdapter
import com.example.evaluation.databinding.ActivityExamDownBinding
import com.example.evaluation.logic.*
import com.example.evaluation.logic.network.EvaluationNetwork
import com.example.evaluation.utils.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ExamDownActivity : AppCompatActivity() {
    private lateinit var binding: ActivityExamDownBinding
    private var examList=ArrayList<Question>()
    private lateinit var adapter: ExamTitleAdapter
    private var username:String=""
    private var name:String=""
    private var score=ArrayList<Map<String,Int>>()
    private val sharePrefs= SharePrefs()
    private var token:String=sharePrefs.getToken(MyApplication.context,"token").toString()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExamDownBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Log.d("ExamDownActivity", "onCreate: ascassssssssssssssssssss")
        setAndroidNativeLightStatusBar(this, true)
        CoroutineScope(Dispatchers.Main).launch {
            getPerformanceQuestions()
        }
        val prefs = getSharedPreferences("data", Context.MODE_PRIVATE)
        name=prefs.getString("name","").toString()
        val intent= intent
        binding.examDownName.text=intent.getStringExtra("name")
        username=intent.getStringExtra("username").toString()

        binding.examDownButton.setOnClickListener (object : OnMultiClickListener() {
            override fun onMultiClick(v: View){
                if(score.size!=5){
                    "请完成所有评分".showToast()
                    return
                }
                CoroutineScope(Dispatchers.Main).launch {
                    postExam()
                }
            }
        })
    }
    private fun setAndroidNativeLightStatusBar(activity: Activity, dark: Boolean) {
        val decor = activity.window.decorView
        if (dark) {
            decor.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)
        } else {
            decor.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE)
        }
    }
    private fun initList(){

    }
    private suspend fun postExam(){
        try {
            Log.d("TAG", "postExam: $score"+"    $username")
            val response: LoginResponse = EvaluationNetwork.postExam(
                token, username,PERFORMANCE_RECORD,PerformanceMap(score)
            )

            Log.d("TAG", "commit: $score")
            if(response.code!=200){
                CoroutineScope(Dispatchers.Main).launch {
                    response.message.showToast()
                }
            }
            if(response.code==200){
                CoroutineScope(Dispatchers.Main).launch {
                    response.message.showToast()
                }
                val intent= Intent(this,ExamDownEmployeeActivity::class.java)
                setResult(RESULT_OK,intent)
                finish()
            }
        }catch(e: Exception) {
            Log.e("TAG", "InformationActivity: ", e)
        }
    }
    private suspend fun getPerformanceQuestions(){
        try {
            val response: GetPerformanceQuestionResponse= EvaluationNetwork.getPerformanceQuestion(token)
            if(response.code!=200){
                CoroutineScope(Dispatchers.Main).launch {
                    response.message.showToast()
                }
            }else{
                for (i in 0 until response.data.size){
                    examList.add(Question(response.data[i].question,response.data[i].score))
                }
                adapter= ExamTitleAdapter(examList,this,score)
                binding.examTest.layoutManager= LinearLayoutManager(this)
                binding.examTest.adapter=adapter
            }
        }catch(e: Exception) {
            Log.e("TAG", "InformationActivity: ", e)
        }
    }

}