package com.example.evaluation.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.media.Image
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.evaluation.R
import com.example.evaluation.activity.*
import com.example.evaluation.logic.ExamData
import com.example.evaluation.logic.GetAvaterResponse
import com.example.evaluation.logic.network.EvaluationNetwork
import com.example.evaluation.utils.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ExamAdapter(private val employeeList:ArrayList<ExamData>,private val context: Context): RecyclerView.Adapter<ExamAdapter.ViewHolder>(){
    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val ava: ImageView =view.findViewById(R.id.employee_ava)
        val name: TextView =view.findViewById(R.id.employee_name)
        val position: TextView =view.findViewById(R.id.employeeNumber)
        val evaluated:TextView=view.findViewById(R.id.evaluation_flag)
    }
    private val sharePrefs= SharePrefs()
    private var token:String=sharePrefs.getToken(MyApplication.context,"token").toString()

    override fun getItemCount()=employeeList.size
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val employee:ExamData=employeeList[position]
        holder.name.text=employee.evaluateeName
        holder.position.text=(position+1).toString()
        CoroutineScope(Dispatchers.Main).launch {
            getAva(holder.ava)
        }
        if(employee.evaluated){
            holder.evaluated.text="已完成"
            holder.itemView.setOnClickListener {
                employee.evaluated=false
                val intent=Intent(context,ExamDownActivity::class.java)
                intent.putExtra("name",employee.evaluateeName)
                intent.putExtra("username",employee.evaluatee)
                (context as ExamDownEmployeeActivity).startActivityForResult(intent, position)
            }
        }else{
            holder.itemView.setOnClickListener {
                employee.evaluated=false
                        val intent=Intent(context,ExamDownActivity::class.java)
                        intent.putExtra("name",employee.evaluateeName)
                        intent.putExtra("username",employee.evaluatee)
                        (context as ExamDownEmployeeActivity).startActivityForResult(intent, position)
                }
            }
        }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(View.inflate(parent.context, R.layout.item_employee, null))
    }
    private fun displayImage(imagePath: String,ava:ImageView) {
        if (!TextUtils.isEmpty(imagePath)) {
            //显示图片
            Glide.with(context).load(imagePath).apply(requestOptions).into(ava)
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
    private val requestOptions = RequestOptions.circleCropTransform()
        .diskCacheStrategy(DiskCacheStrategy.NONE) //不做磁盘缓存
        .skipMemoryCache(true) //不做内存缓存
    private suspend fun getAva(ava:ImageView){
        try {
            val response: GetAvaterResponse = EvaluationNetwork.getAvater(token)
            if(response.code!=200){
                CoroutineScope(Dispatchers.Main).launch {
                    response.message.showToast()
                }
            }
            if(response.code==200){
                /*if(response.data!=null){
                    displayImage("http://110.41.60.28:81/avatar/"+response.data,ava)
                }*/
                displayImage("http://10.48.27.15:8801/avatar/"+response.data,ava)
            }
        }catch(e: Exception) {
            Log.e("TAG", "InformationActivity: ", e)
        }
    }
}