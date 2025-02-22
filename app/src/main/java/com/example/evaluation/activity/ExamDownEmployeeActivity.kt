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
import com.example.evaluation.adapter.EmployeeAdapter
import com.example.evaluation.adapter.ExamAdapter
import com.example.evaluation.databinding.ActivityExamDownBinding
import com.example.evaluation.databinding.ActivityExamDownEmplyeeBinding
import com.example.evaluation.databinding.ActivityExamSameBinding
import com.example.evaluation.logic.*
import com.example.evaluation.logic.network.EvaluationNetwork
import com.example.evaluation.utils.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ExamDownEmployeeActivity : AppCompatActivity() {
    private lateinit var binding:ActivityExamDownEmplyeeBinding
    private var list=ArrayList<ExamData>()
    private val sharePrefs= SharePrefs()
    private var token:String=sharePrefs.getToken(MyApplication.context,"token").toString()

    private lateinit var adapter: ExamAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityExamDownEmplyeeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setAndroidNativeLightStatusBar(this, true)
        //init()

        CoroutineScope(Dispatchers.Main).launch {
            getExamDown()
        }

    }

    private fun setAndroidNativeLightStatusBar(activity: Activity, dark: Boolean) {
        val decor = activity.window.decorView
        if (dark) {
            decor.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)
        } else {
            decor.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE)
        }
    }
    private suspend fun getExamDown(){
        try {
           val response:GetExamList= EvaluationNetwork.getEmployees(token, PERFORMANCE_RECORD)
            if(response.code!=200){
                CoroutineScope(Dispatchers.Main).launch {
                    response.message.showToast()
                }
            }
            if(response.code==200){
                val prefs = getSharedPreferences("data", Context.MODE_PRIVATE)
                val state=prefs.getBoolean("ExamState",false)
                if(!state){
                    list=response.data
                    adapter= ExamAdapter(list,this)
                    binding.examDownEmployee.layoutManager= LinearLayoutManager(this)
                    binding.examDownEmployee.adapter=adapter
                        set()
                }else{
                    list=SharedPreferencesUtils.getExamList(this)
                    Log.d("TAG", "getExamDown: "+list)
                    adapter= ExamAdapter(list,this)
                    binding.examDownEmployee.layoutManager= LinearLayoutManager(this)
                    binding.examDownEmployee.adapter=adapter
                }
            }
        }catch(e: Exception) {
            Log.e("TAG", "InformationActivity: ", e)
        }
    }
    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode== Activity.RESULT_OK){
            list[requestCode].evaluated=true
            adapter.notifyItemChanged(requestCode)
            SharedPreferencesUtils.saveExamList(this,list)
        }
    }
    override fun onBackPressed() {
        if(list.isEmpty()){
            super.onBackPressed()
        }else{
            var flag=true
            for(i in list){
                if(!i.evaluated){
                    flag=false
                    break
                }
            }
            if(flag){
                    complete()
            }
            super.onBackPressed()
        }

    }
    private  fun set(){
            //val response: LoginResponse = EvaluationNetwork.setPerformanceCompletionStatus(token)
            val prefs = getSharedPreferences("data", Context.MODE_PRIVATE)
            val editor = prefs.edit()
            editor.putBoolean("ExamState",true)
            editor.apply()
            SharedPreferencesUtils.saveExamList(this,list)

    }
    private suspend fun complete1(){
        try {
            val response: LoginResponse = EvaluationNetwork.completePerformance(token)
            if(response.code!=200){
                CoroutineScope(Dispatchers.Main).launch {
                    response.message.showToast()
                }
            }
        }catch(e: Exception) {
            Log.e("TAG", "InformationActivity: ", e)
        }
    }
    private  fun complete(){
        CoroutineScope(Dispatchers.Main).launch {
            complete1()
        }
    }
}