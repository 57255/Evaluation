package com.example.evaluation.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import com.example.evaluation.MainActivity
import com.example.evaluation.databinding.ActivityEditRankBinding
import com.example.evaluation.logic.GetAllPositionResponse
import com.example.evaluation.logic.GetEmployeeInformation
import com.example.evaluation.logic.LoginResponse
import com.example.evaluation.logic.network.EvaluationNetwork
import com.example.evaluation.utils.MyApplication
import com.example.evaluation.utils.SharePrefs
import com.example.evaluation.utils.showToast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EditRankActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditRankBinding
    private val sharePrefs= SharePrefs()
    private var token:String=sharePrefs.getToken(MyApplication.context,"token").toString()

    /*private val sItems= arrayOf("中层干部（包含值长、主任助理）","管理部门高级主管","管理部门主管（包含二级主管）","单元长、机组长","集控主值","集控副值、辅控主值",
    "辅控副值、辅控巡操","维护部班长、副班长、技术员","维护部专责工与检修工","设备管理部班长、副班长、技术员",
    "设备管理部专责工与检修工","设备管理部点检长、高级点检员、室主任","设备管理部点检员","发电部、设备管理部、维护部 高级主管、主管、专工","发电部、燃料质检部化验班、采样班、操作班、煤质监督班所有岗位",
        "公司领导","管理部门中层干部","值长、发电部中层干部","集控主值","集控副值、辅控主值、集控主值","维护部中层干部、维护部专工",
    "维护部班长、副班长、技术员、维护部专工","设备管理部中层干部、设备管理部专工","设备管理部班长、副班长、技术员、设备管理部专工","设备管理部中层干部",
    "设备管理部点检长、高级点检员、室主任","发电部、设备管理部、维护部 中层干部","发电部、燃料质检部中层干部","职工代表",
    "管理部门主管","集控主值","集控副值、辅控主值","辅控副值、辅控巡操","维护部专责工、检修工","设备管理部专责工、检修工","设备管理部点检员")*/
    private lateinit var sItems: Array<String>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditRankBinding.inflate(layoutInflater)
        setContentView(binding.root)
        CoroutineScope(Dispatchers.Main).launch {
            getPosition()
        }
        var mCheckedItem:Int=0
        binding.choiceEvaluationLayout.setOnClickListener {
            val builder = AlertDialog.Builder(this)
            builder.setTitle("请选择岗位层级")
            builder.setSingleChoiceItems(sItems, mCheckedItem) { dialog, which ->
                mCheckedItem = which
            }
            builder.setPositiveButton("确定") { dialog, which ->
                binding.evaluationRank1.text = sItems[mCheckedItem]
                CoroutineScope(Dispatchers.Main).launch {
                    setRank()
                }
            }
            builder.setNegativeButton("取消") { dialog, which -> }
            builder.show()
        }
    }
    private suspend fun setRank(){
        try {
            val response: LoginResponse = EvaluationNetwork.updateRank(token,binding.evaluationRank1.text.toString())
            if(response.code!=200){
                CoroutineScope(Dispatchers.Main).launch {
                    response.message.showToast()
                }
            }
            if(response.code==200){
                val intent = Intent(this, PhoneActivity::class.java)
                startActivity(intent)
                finish()
            }
        }catch(e: Exception) {
            Log.e("TAG", "InformationActivity: ", e)
        }
    }
    private suspend fun getPosition(){
        try {
            val response: GetAllPositionResponse= EvaluationNetwork.getAllPosition(token)
            if(response.code!=200){
                CoroutineScope(Dispatchers.Main).launch {
                    response.message.showToast()
                }
            }
            if(response.code==200){
                sItems= arrayOf()
                for(i in response.data){
                    sItems+=i
                }
            }
        }catch(e: Exception) {
            Log.e("TAG", "InformationActivity: ", e)
        }
    }
}