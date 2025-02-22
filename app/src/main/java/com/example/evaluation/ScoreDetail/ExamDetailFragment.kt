package com.example.evaluation.ScoreDetail

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.evaluation.R
import com.example.evaluation.adapter.EvaluationAdapter
import com.example.evaluation.adapter.EvaluationDetailAdapter
import com.example.evaluation.databinding.FragmentExamBinding
import com.example.evaluation.databinding.FragmentExamDetailBinding
import com.example.evaluation.logic.*
import com.example.evaluation.logic.network.EvaluationNetwork
import com.example.evaluation.utils.*
import kotlinx.coroutines.*

class ExamDetailFragment : Fragment() {

    private  var _binding: FragmentExamDetailBinding?=null
    private val binding get() = _binding!!
    private lateinit var adapter: EvaluationDetailAdapter
    private  var evaluationList=ArrayList<Evaluation>()
    private val sharePrefs= SharePrefs()
    private val  l=ArrayList<Evaluation>()
    private var token:String=sharePrefs.getToken(MyApplication.context,"token").toString()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding= FragmentExamDetailBinding.inflate(inflater,container,false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        CoroutineScope(Dispatchers.Main).launch {
            getEvaluationRecord()
        }
        /*evaluationList.add(Evaluation("","电厂员工4",86))
        evaluationList.add(Evaluation("","电厂员工5",85))
        evaluationList.add(Evaluation("","电厂员工6",84))
        evaluationList.add(Evaluation("","电厂员工7",83))
        evaluationList.add(Evaluation("","电厂员工8",82))
        evaluationList.add(Evaluation("","电厂员工9",81))
        evaluationList.add(Evaluation("","电厂员工10",80))
        evaluationList.add(Evaluation("","电厂员工11",79))
        evaluationList.add(Evaluation("","电厂员工12",78))
        evaluationList.add(Evaluation("","电厂员工13",77))
        evaluationList.add(Evaluation("","电厂员工14",76))
        evaluationList.add(Evaluation("","电厂员工15",75))
        evaluationList.add(Evaluation("","电厂员工16",74))
        evaluationList.add(Evaluation("","电厂员工17",73))*/
    }
    private suspend fun getEvaluationRecord(){
        try {
            val response: GetPerformanceTotalScore = EvaluationNetwork.getPerformanceRecordDetail(
                token,
                PERFORMANCE_RECORD
            )
            //("$PERFORMANCE_RECORD").showToast()
            if (response.code != 200) {
                CoroutineScope(Dispatchers.Main).launch {
                    response.message.showToast()
                }
            }
            if (response.code == 200) {
                for (i in 0 until response.data.size) {
                    evaluationList.add(Evaluation("","电厂员工",response.data[i].positionCompetencyScore))

                }
                //("尺寸：+${response.data.size}").showToast()
                val deferreds = (0 until evaluationList.size).map { i ->
                    CoroutineScope(Dispatchers.IO).async {
                        getRank(response.data[i].assessesNumber, i)
                    }
                }
                // 等待所有 getRank 请求完成
                deferreds.awaitAll()
                var flag = 1
                if(evaluationList.size<10){
                    for(i in 0 until evaluationList.size){
                        l.add(evaluationList[i])
                    }
                }else{
                    for(i in 0 until 10){
                        l.add(evaluationList[i])
                    }
                }
                adapter= EvaluationDetailAdapter(l,requireContext())
                binding.exam.layoutManager= LinearLayoutManager(requireContext())
                binding.exam.adapter=adapter
                adapter.setOnLoadMoreListener(object : EvaluationDetailAdapter.OnLoadMoreListener {
                    override fun onLoadMore() {
                        val batchSize = 10 // 每次加载的数据量
                        val startIndex = flag * batchSize
                        val endIndex = minOf((flag + 1) * batchSize, evaluationList.size)
                        for (i in startIndex until endIndex) {
                            adapter.insertEvaluation(evaluationList[i])
                        }
                        flag++
                    }
                })
            }
        }catch(e:Exception){
            e.printStackTrace()
        }
    }
    private suspend fun getRank(username:String,position:Int){
        try {
            val response: GetEmployeeInformation = EvaluationNetwork.getInfo(token,username)
            /*if(response.code!=200){
                CoroutineScope(Dispatchers.Main).launch {
                    response.message.showToast()
                }
            }*/
            if(response.code==200){
                Log.d("TAG", "getRank: "+response.data.avatar)
                if(response.data.avatar!=null){
                    evaluationList[position].avatar=response.data.avatar
                }
                evaluationList[position].fullName=response.data.fullName
            }
        }catch(e: Exception) {
            Log.e("TAG", "InformationActivity: ", e)
        }
    }
}