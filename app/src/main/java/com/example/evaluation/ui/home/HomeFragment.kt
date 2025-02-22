package com.example.evaluation.ui.home

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.evaluation.adapter.NoticeAdapter
import com.example.evaluation.databinding.FragmentHomeBinding
import com.example.evaluation.logic.*
import com.example.evaluation.logic.network.EvaluationNetwork
import com.example.evaluation.utils.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val noticeList = ArrayList<String>()
    private lateinit var adapter: NoticeAdapter
    private val binding get() = _binding!!
    private lateinit var rec:RecyclerView
    private val sharePrefs= SharePrefs()
    private var token:String=sharePrefs.getToken(MyApplication.context,"token").toString()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //noticeList.add("这是一个很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长很长的通知!!!")


        CoroutineScope(Dispatchers.Main).launch {
            getCompany()
        }
        CoroutineScope(Dispatchers.Main).launch {
            getEmployee()
        }
        CoroutineScope(Dispatchers.Main).launch {
            getNotice()
        }
        CoroutineScope(Dispatchers.Main).launch {
            getEvaluation()
        }
        CoroutineScope(Dispatchers.Main).launch {
            getPerformance()
        }

    }
    private val handler: Handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            rec.scrollBy(rec.scrollX + 6,rec.scrollY)

            this.sendEmptyMessageDelayed(0x00, 100)
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    private fun displayImage(imagePath: String, imagine: ImageView) {
        if (!TextUtils.isEmpty(imagePath)) {
            //显示图片
            Glide.with(this).load(imagePath).apply(requestOptions).into(imagine)
/*
            //压缩图片
            orc_bitmap = CameraUtils.compression(BitmapFactory.decodeFile(imagePath))!!
            //转Base64
            base64Pic = BitmapUtils.bitmapToBase64(orc_bitmap)*/
        } else {
            ("图片获取失败").showToast()
        }
    }
    private lateinit var base64Pic: String

    //拍照和相册获取图片的Bitmap
    private lateinit var orc_bitmap: Bitmap

    //Glide请求图片选项配置
    private val requestOptions = RequestOptions.centerCropTransform()
        .diskCacheStrategy(DiskCacheStrategy.NONE) //不做磁盘缓存
        .skipMemoryCache(true) //不做内存缓存

    private suspend fun getCompany(){
        try {
            val response:GetEmployeeAvaterResponse = EvaluationNetwork.getHomeCompanyImage(token)
            /*if(response.code!=200){
                CoroutineScope(Dispatchers.Main).launch {
                    response.message.showToast()
                }
            }*/
            if(response.code==200){
                displayImage("http://110.41.60.28:81/HomeCompanyImage/"+response.data[response.data.size-1],binding.companyImage)
            }
        }catch(e: Exception) {
            Log.e("TAG", "InformationActivity: ", e)
        }
    }
    private suspend fun getNotice(){
        try {
            val response:GetAvaterResponse = EvaluationNetwork.getHomeAnnouncement(token)
            /*if(response.code!=200){
                CoroutineScope(Dispatchers.Main).launch {
                    response.message.showToast()
                }
            }*/
            if(response.code==200){
                noticeList.add(response.data)
                rec=binding.noticeRecyclerview
                adapter= NoticeAdapter(noticeList)
                binding.noticeRecyclerview.layoutManager= LinearLayoutManager(requireContext(),LinearLayoutManager.HORIZONTAL,false)
                binding.noticeRecyclerview.adapter=adapter
                handler.sendEmptyMessageDelayed(0x00,1000)
            }
        }catch(e: Exception) {
            Log.e("TAG", "InformationActivity: ", e)
        }
    }
    private suspend fun getEmployee(){
        try {
            val response:GetEmployeeAvaterResponse = EvaluationNetwork.getHomeEmployeePicture(token)
            /*if(response.code!=200){
                CoroutineScope(Dispatchers.Main).launch {
                    response.message.showToast()
                }
            }*/
            if(response.code==200){
                displayImage("http://110.41.60.28:81/HomeEmployeeImage/"+response.data[response.data.size-1],binding.employeeImage1)
                displayImage("http://110.41.60.28:81/HomeEmployeeImage/"+response.data[response.data.size-2],binding.employeeImage2)
                displayImage("http://110.41.60.28:81/HomeEmployeeImage/"+response.data[response.data.size-3],binding.employeeImage3)
                displayImage("http://110.41.60.28:81/HomeEmployeeImage/"+response.data[response.data.size-4],binding.employeeImage4)
                displayImage("http://110.41.60.28:81/HomeEmployeeImage/"+response.data[response.data.size-5],binding.employeeImage5)
                displayImage("http://110.41.60.28:81/HomeEmployeeImage/"+response.data[response.data.size-6],binding.employeeImage6)
            }
        }catch(e: Exception) {
            Log.e("TAG", "InformationActivity: ", e)
        }
    }
    private suspend fun getEvaluation(){
        try {
            val response:GetEvaluationInfoResponse= EvaluationNetwork.getEvaluationInfo(token)
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
                    editor.putBoolean("state1Same",true)
                    editor.putBoolean("state1Up",true)
                    editor.putBoolean("state1Down",true)
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
            val response:GetPerformanceRecordResponse= EvaluationNetwork.getPerformanceRecord(token)
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