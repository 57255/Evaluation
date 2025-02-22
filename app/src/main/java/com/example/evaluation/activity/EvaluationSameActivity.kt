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
import com.example.evaluation.adapter.EvaluationTitleAdapter
import com.example.evaluation.databinding.ActivityEvaluationSameBinding
import com.example.evaluation.logic.*
import com.example.evaluation.logic.network.EvaluationNetwork
import com.example.evaluation.logic.network.network
import com.example.evaluation.utils.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EvaluationSameActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEvaluationSameBinding
    private var evaluationTitleList=ArrayList<EvaluationTitle>()
    private lateinit var adapter: EvaluationTitleAdapter
    private val sharePrefs= SharePrefs()
    private var username:String=""
    private var score=ArrayList<Int>()
    private var level:String=""
    private var token:String=sharePrefs.getToken(MyApplication.context,"token").toString()
    private var AssessorName:String=""
    private var questionScorePairs=ArrayList<QuestionScorePairs>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEvaluationSameBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setAndroidNativeLightStatusBar(this, true)
        val prefs = getSharedPreferences("data", Context.MODE_PRIVATE)
        AssessorName=prefs.getString("name","").toString()
        //initList()
        /*evaluationTitleList.add(EvaluationTitle("a",1,1,"accs",14,1))
        evaluationTitleList.add(EvaluationTitle("b",2,2,"accs",14,1))
        evaluationTitleList.add(EvaluationTitle("c",3,3,"accs",14,1))
        evaluationTitleList.add(EvaluationTitle("d",4,4,"accs",14,1))
        evaluationTitleList.add(EvaluationTitle("e",5,5,"accs",14,1))
*/        CoroutineScope(Dispatchers.Main).launch {
            /*getTitle("岗位履职能力")
            getTitle2("德勤")*/
            val intent=intent
            level=intent.getStringExtra("rank").toString()
            binding.evaluationSameName.text=intent.getStringExtra("name")
            username=intent.getStringExtra("username").toString()

        }
        CoroutineScope(Dispatchers.Main).launch {
            getTitle3()
        }
        adapter= EvaluationTitleAdapter(evaluationTitleList,this,score)

        binding.evaluationTestSame.layoutManager= LinearLayoutManager(this)
        binding.evaluationTestSame.adapter=adapter
        binding.evaluationSameButton.setOnClickListener(object : OnMultiClickListener() {
            override fun onMultiClick(v: View){
                if (score.size!=evaluationTitleList.size){
                    Log.d("EvaluationSameActivity", "onCreate: ${score.size} ${evaluationTitleList.size}")
                    "请先完成所有评分".showToast()
                    return
                }
                for(i in 0 until evaluationTitleList.size){
                    questionScorePairs.add(QuestionScorePairs(evaluationTitleList[i].evaluationId, score[i],evaluationTitleList[i].category,AssessorName,username))
                }
                Log.d("EvaluationDownActivity", "onCreate: $questionScorePairs")
                CoroutineScope(Dispatchers.Main).launch {
                    commit()
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
        for (i in 0..20){
            evaluationTitleList.add(EvaluationTitle("",1,1,"Evaluation Title $i",1,0))
        }
    }
    private suspend fun getTitle(category:String){
        try {
            val response: GetEvaluationTitleResponse = EvaluationNetwork.getEvaluationTitle(token,category)
            if(response.code!=200){
                CoroutineScope(Dispatchers.Main).launch {
                    response.message.showToast()
                }
            }
            if(response.code==200){
                evaluationTitleList=response.data
            }
        }catch(e: Exception) {
            Log.e("TAG", "InformationActivity: ", e)
        }
    }
    private suspend fun getTitle2(category:String){
        try {
            val response:GetEvaluationTitleResponse= EvaluationNetwork.getEvaluationTitle(token,category)
            if(response.code!=200){
                CoroutineScope(Dispatchers.Main).launch {
                    response.message.showToast()
                }
            }
            if(response.code==200){
                for(i in 0 until response.data.size){
                    evaluationTitleList.add(response.data[i])
                }
                adapter= EvaluationTitleAdapter(evaluationTitleList,this,score)
                binding.evaluationTestSame.layoutManager= LinearLayoutManager(this)
                binding.evaluationTestSame.adapter=adapter
            }
        }catch(e: Exception) {
            Log.e("TAG", "InformationActivity: ", e)
        }
    }
    private suspend fun commit(){
        try {
            val response: LoginResponse = EvaluationNetwork.postEvaluation(
                RECORD_ID,token,
                PostEvaluationResult(questionScorePairs)
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
                val intent= Intent(this,EvaluationSameEmployeeActivity::class.java)
                setResult(RESULT_OK,intent)
                finish()
            }
        }catch(e: Exception) {
            Log.e("TAG", "InformationActivity: ", e)
        }
    }
    private suspend fun getTitle3(){
        try {
            val response:GetEvaluationTitleResponse= network.getTitle3(level,token)
            if(response.code!=200){
                CoroutineScope(Dispatchers.Main).launch {
                    response.message.showToast()
                }
            }
            if(response.code==200){
                for(i in 0 until response.data.size){
                    evaluationTitleList.add(response.data[i])
                }
                adapter= EvaluationTitleAdapter(evaluationTitleList,this,score)
                binding.evaluationTestSame.layoutManager= LinearLayoutManager(this)
                binding.evaluationTestSame.adapter=adapter
            }
        }catch(e: Exception) {
            Log.e("TAG", "InformationActivity: ", e)
        }
    }

}