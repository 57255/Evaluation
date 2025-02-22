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
import com.example.evaluation.databinding.ActivityEvaluationSameEmployeeBinding
import com.example.evaluation.logic.Employee
import com.example.evaluation.logic.Evaluation
import com.example.evaluation.logic.GetSuperiorResponse
import com.example.evaluation.logic.LoginResponse
import com.example.evaluation.logic.network.EvaluationNetwork
import com.example.evaluation.utils.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EvaluationSameEmployeeActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEvaluationSameEmployeeBinding
    private var list=ArrayList<Employee>()
    private val sharePrefs= SharePrefs()
    private var token:String=sharePrefs.getToken(MyApplication.context,"token").toString()
    private lateinit var adapter: EmployeeAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEvaluationSameEmployeeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setAndroidNativeLightStatusBar(this, true)
        //init()
        CoroutineScope(Dispatchers.Main).launch {
            getSame()
        }

    }
    private fun init(){
        list.add(Employee(" ",false,"",1,"电厂员工1","",false,"","","1",false,""))
        list.add(Employee(" ",false,"",1,"电厂员工2","",false,"","","1",false,""))
        list.add(Employee(" ",false,"",1,"电厂员工3","",false,"","","1",false,""))
        list.add(Employee(" ",false,"",1,"电厂员工4","",false,"","","1",false,""))
        list.add(Employee(" ",false,"",1,"电厂员工5","",false,"","","1",false,""))
        list.add(Employee(" ",false,"",1,"电厂员工6","",false,"","","1",false,""))
        list.add(Employee(" ",false,"",1,"电厂员工7","",false,"","","1",false,""))
        list.add(Employee(" ",false,"",1,"电厂员工8","",false,"","","1",false,""))
        list.add(Employee(" ",false,"",1,"电厂员工9","",false,"","","1",false,""))
        list.add(Employee(" ",false,"",1,"电厂员工10","",false,"","","1",false,""))
        list.add(Employee(" ",false,"",1,"电厂员工11","",false,"","","1",false,""))
        list.add(Employee(" ",false,"",1,"电厂员工12","",false,"","","1",false,""))
        list.add(Employee(" ",false,"",1,"电厂员工13","",false,"","","1",false,""))
        list.add(Employee(" ",false,"",1,"电厂员工14","",false,"","","1",false,""))
        list.add(Employee(" ",false,"",1,"电厂员工15","",false,"","","1",false,""))
        list.add(Employee(" ",false,"",1,"电厂员工16","",false,"","","1",false,""))
        list.add(Employee(" ",false,"",1,"电厂员工17","",false,"","","1",false,""))
        list.add(Employee(" ",false,"",1,"电厂员工18","",false,"","","1",false,""))
        list.add(Employee(" ",false,"",1,"电厂员工19","",false,"","","1",false,""))
        adapter= EmployeeAdapter(list, EVALUATION_SAME,this)
        binding.evaluationSameEmployee.layoutManager= LinearLayoutManager(this)
        binding.evaluationSameEmployee.adapter=adapter
    }
    private fun setAndroidNativeLightStatusBar(activity: Activity, dark: Boolean) {
        val decor = activity.window.decorView
        if (dark) {
            decor.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)
        } else {
            decor.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE)
        }
    }
    private suspend fun getSame(){
        try {
            val response: GetSuperiorResponse = EvaluationNetwork.getSameLevel(token, RECORD_ID)
            if(response.code!=200){
                CoroutineScope(Dispatchers.Main).launch {
                    response.message.showToast()
                }
            }
            if(response.code==200){
                val prefs = getSharedPreferences("data", Context.MODE_PRIVATE)
                val state=prefs.getBoolean("state",false)
                val state1Same=prefs.getBoolean("state1Same",false)
                if(!state||state1Same){
                    list=response.data
                        set()
                    adapter= EmployeeAdapter(list, EVALUATION_SAME,this)
                    binding.evaluationSameEmployee.layoutManager= LinearLayoutManager(this)
                    binding.evaluationSameEmployee.adapter=adapter
                }else{
                    list=SharedPreferencesUtils.getEmployeeListSame(this)
                    adapter= EmployeeAdapter(list, EVALUATION_SAME,this)
                    binding.evaluationSameEmployee.layoutManager= LinearLayoutManager(this)
                    binding.evaluationSameEmployee.adapter=adapter
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
            Log.d("dsacaaaaaaaaaaa",requestCode.toString())
            SharedPreferencesUtils.saveEmployeeListSame(this,list)
        }
    }
    /*override fun onBackPressed() {
        if(list.isEmpty()){
            complete()
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
            else{
                AlertDialog.Builder(this)
                    .setTitle("提示")
                    .setMessage("您还有未评价的员工，现在退出将无法记录，是否退出？")
                    .setPositiveButton("确定") { _, _ ->
                        super.onBackPressed()
                    }
                    .setNegativeButton("取消") {_,_   ->
                    }
                    .show()
            }

        }
    }*/
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
                val prefs = getSharedPreferences("data", Context.MODE_PRIVATE)
                val editor = prefs.edit()
                editor.putBoolean("state",true)
                //editor.putBoolean("stateUp",false)
                //editor.putBoolean("stateDown",false)
                editor.putBoolean("stateSame",false)
        editor.putBoolean("state1Same",false)
                editor.apply()
        SharedPreferencesUtils.saveEmployeeListSame(this,list)
    }
    private  fun complete(){
        val prefs = getSharedPreferences("data", Context.MODE_PRIVATE)
        val editor = prefs.edit()
        editor.putBoolean("stateSame",true)
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