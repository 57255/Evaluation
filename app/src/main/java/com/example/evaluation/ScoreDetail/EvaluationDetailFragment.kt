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
import com.example.evaluation.databinding.FragmentEvaluationBinding
import com.example.evaluation.databinding.FragmentEvaluationDetailBinding
import com.example.evaluation.logic.*
import com.example.evaluation.logic.network.EvaluationNetwork
import com.example.evaluation.utils.RECORD_ID
import com.example.evaluation.utils.SharePrefs
import com.example.evaluation.utils.showToast
import kotlinx.coroutines.*

class EvaluationDetailFragment : Fragment() {
    private  var _binding: FragmentEvaluationDetailBinding?=null
    private val binding get() = _binding!!
    private lateinit var adapter: EvaluationDetailAdapter
    private val sharePrefs= SharePrefs()
    private var token:String=sharePrefs.getToken(com.example.evaluation.utils.MyApplication.context,"token").toString()
    private val  l=ArrayList<Evaluation>()

    private  var evaluationList=ArrayList<Evaluation>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding= FragmentEvaluationDetailBinding.inflate(inflater,container,false)
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
            val response: GetEvaluationRecordResponse = EvaluationNetwork.getAllEvaluationRecord(
                token,
                RECORD_ID
            )
            if (response.code != 200) {
                CoroutineScope(Dispatchers.Main).launch {
                    response.message.showToast()
                }
            }
            if (response.code == 200) {
                for (i in 0 until response.data.size) {
                    evaluationList.add(Evaluation("","电厂员工",response.data[i].totalScore))
                }
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
                binding.evaluation.layoutManager= LinearLayoutManager(requireContext())
                binding.evaluation.adapter=adapter
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
            val response: GetEmployeeInformation= EvaluationNetwork.getInfo(token,username)
            /*if(response.code!=200){
                CoroutineScope(Dispatchers.Main).launch {
                    response.message.showToast()
                }
            }*/
            if(response.code==200){
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