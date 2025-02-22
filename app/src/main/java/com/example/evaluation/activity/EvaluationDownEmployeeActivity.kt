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
import com.example.evaluation.databinding.ActivityEvaluationDownEmployeeBinding
import com.example.evaluation.logic.Employee
import com.example.evaluation.logic.Evaluation
import com.example.evaluation.logic.GetSuperiorResponse
import com.example.evaluation.logic.LoginResponse
import com.example.evaluation.logic.network.EvaluationNetwork
import com.example.evaluation.utils.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EvaluationDownEmployeeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEvaluationDownEmployeeBinding
    private var list=ArrayList<Employee>()
    private val sharePrefs= SharePrefs()
    private var token:String=sharePrefs.getToken(MyApplication.context,"token").toString()
    private lateinit var adapter: EmployeeAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEvaluationDownEmployeeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setAndroidNativeLightStatusBar(this, true)
        CoroutineScope(Dispatchers.Main).launch {
            getDown()
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
    private suspend fun getDown(){
        try {
            val response:GetSuperiorResponse = EvaluationNetwork.getSubordinates(token, RECORD_ID)
            if(response.code!=200){
                CoroutineScope(Dispatchers.Main).launch {
                    response.message.showToast()
                }
            }
            if(response.code==200){
                val prefs = getSharedPreferences("data", Context.MODE_PRIVATE)
                val state=prefs.getBoolean("state",false)
                val state1Down=prefs.getBoolean("state1Down",false)
                if(!state||state1Down){
                    list=response.data
                    set()
                    adapter= EmployeeAdapter(list, EVALUATION_DOWN,this)
                    binding.evaluationDownEmployee.layoutManager= LinearLayoutManager(this)
                    binding.evaluationDownEmployee.adapter=adapter
                }else{
                    list=SharedPreferencesUtils.getEmployeeList(this)
                    adapter= EmployeeAdapter(list, EVALUATION_DOWN,this)
                    binding.evaluationDownEmployee.layoutManager= LinearLayoutManager(this)
                    binding.evaluationDownEmployee.adapter=adapter
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
            SharedPreferencesUtils.saveEmployeeList(this,list)
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
    private fun set(){
            //val response: LoginResponse = EvaluationNetwork.setCompletionStatus(token)
                val prefs = getSharedPreferences("data", Context.MODE_PRIVATE)
                val editor = prefs.edit()
                editor.putBoolean("state",true)
                //editor.putBoolean("stateUp",false)
                editor.putBoolean("stateDown",false)
                //editor.putBoolean("stateSame",false)
        //editor.putBoolean("state1Up",false)
        editor.putBoolean("state1Down",false)
        //editor.putBoolean("state1Same",false)
                editor.apply()
SharedPreferencesUtils.saveEmployeeList(this,list)
    }
    private  fun complete(){
                val prefs = getSharedPreferences("data", Context.MODE_PRIVATE)
                val editor = prefs.edit()
                editor.putBoolean("stateDown",true)
                editor.apply()
                if(prefs.getBoolean("stateDown",false)&&prefs.getBoolean("stateUp",false)&&prefs.getBoolean("stateSame",false)){
                    CoroutineScope(Dispatchers.Main).launch {
                        completeEvaluation()
                    }
                }else{
                    super.onBackPressed()
                }
    }
    private suspend fun completeEvaluation(){
        try {
            val response: LoginResponse= EvaluationNetwork.completeEvaluation(token, RECORD_ID)
            if(response.code!=200){
                response.message.showToast()
            }
        }catch(e: Exception) {
            Log.e("TAG", "InformationActivity: ", e)
        }
    }
}