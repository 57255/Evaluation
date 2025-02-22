package com.example.evaluation.ui.dashboard

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.evaluation.activity.*
import com.example.evaluation.databinding.FragmentDashboardBinding
import com.example.evaluation.logic.*
import com.example.evaluation.logic.network.EvaluationNetwork
import com.example.evaluation.utils.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private val sharePrefs= SharePrefs()
    private var token:String=sharePrefs.getToken(com.example.evaluation.utils.MyApplication.context,"token").toString()
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        CoroutineScope(Dispatchers.Main).launch {
            getFinalScore()
        }
        CoroutineScope(Dispatchers.Main).launch {
            getEvaluation()
        }
        CoroutineScope(Dispatchers.Main).launch {
            getPerformance()
        }
        binding.evaluatingSubordinateCardView.setOnClickListener {
            val intent = Intent(activity, EvaluationDownEmployeeActivity::class.java)
            startActivity(intent)
        }
        binding.sameLevelCardView.setOnClickListener {
            val intent = Intent(activity, EvaluationSameEmployeeActivity::class.java)
            startActivity(intent)
        }
        binding.evaluatingSuperiorCardView.setOnClickListener {
            val intent = Intent(activity, EvaluationUpEmployeeActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    private suspend fun getFinalScore(){
        try {
            val response: GetScoreResponse =EvaluationNetwork.getFinalScore(token, RECORD_ID)
            if(response.code==200){
                binding.dialProgressBar.setValue(response.data.toFloat())
                binding.circleProgressView2.setProgress(response.data.toFloat())
                binding.circleProgressView2.setmProgressWidth(10)
            }
        }catch(e: Exception) {
            Log.e("TAG", "InformationActivity: ", e)
        }
    }
    private suspend fun getEvaluation(){
        try {
            val response: GetEvaluationInfoResponse = EvaluationNetwork.getEvaluationInfo(token)
            if(response.code!=200){
                CoroutineScope(Dispatchers.Main).launch {
                    response.message.showToast()
                }
            }
            if(response.code==200){
                RECORD_ID=response.data[response.data.size-1].recordId
                val prefs = MyApplication.context.getSharedPreferences("data", Context.MODE_PRIVATE)
                val editor = prefs.edit()
                val recordId=prefs.getInt("RECORD_ID",-1)
                if(recordId!=RECORD_ID){
                    editor.putInt("RECORD_ID", RECORD_ID)
                    editor.putBoolean("state",false)
                    editor.apply()
                }
                Log.d("TAG", "getEvaluation: "+RECORD_ID)
            }
        }catch(e: Exception) {
            Log.e("TAG", "InformationActivity: ", e)
        }
    }
    private suspend fun getPerformance(){
        try {
            val response: GetPerformanceRecordResponse = EvaluationNetwork.getPerformanceRecord(token)
            if(response.code!=200){
                CoroutineScope(Dispatchers.Main).launch {
                    response.message.showToast()
                }
            }
            if(response.code==200){
                PERFORMANCE_RECORD=response.data[response.data.size-1].performanceRecordId
                val prefs = MyApplication.context.getSharedPreferences("data", Context.MODE_PRIVATE)
                val editor = prefs.edit()
                val recordId=prefs.getInt("PERFORMANCE_RECORD",-1)
                if(recordId!=PERFORMANCE_RECORD){
                    editor.putInt("PERFORMANCE_RECORD", PERFORMANCE_RECORD)
                    editor.putBoolean("ExamState",false)
                    editor.apply()
                }
            }
        }catch(e: Exception) {
            Log.e("TAG", "InformationActivity: ", e)
        }
    }
}