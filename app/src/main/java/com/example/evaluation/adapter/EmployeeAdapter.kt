package com.example.evaluation.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
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
import com.example.evaluation.logic.Employee
import com.example.evaluation.logic.Evaluation
import com.example.evaluation.utils.*

class EmployeeAdapter(private val employeeList:ArrayList<Employee>,private val type:Int,private val context: Context): RecyclerView.Adapter<EmployeeAdapter.ViewHolder>(){
    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val ava: ImageView =view.findViewById(R.id.employee_ava)
        val name: TextView =view.findViewById(R.id.employee_name)
        val position: TextView =view.findViewById(R.id.employeeNumber)
        val evaluated:TextView=view.findViewById(R.id.evaluation_flag)
    }
    override fun getItemCount()=employeeList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val employee=employeeList[position]
        holder.name.text=employee.fullName
        holder.position.text=(position+1).toString()
        /*if(employee.avatar!=null){
        displayImage("http://110.41.60.28:81/avatar/"+employee.avatar,holder.ava)
        }*/
        displayImage("http://110.41.60.28:81/avatar/"+employee.avatar,holder.ava)
        if(employee.evaluated){
            holder.evaluated.text="已完成"
            holder.itemView.setOnClickListener {
                employeeList[position].evaluated=false
                when (type) {
                    EVALUATION_UP -> {
                        val intent=Intent(context,EvaluationUpActivity::class.java)
                        intent.putExtra("name",employee.fullName)
                        intent.putExtra("username",employee.username)
                        intent.putExtra("rank",employee.rank1)
                        Log.d("level:","${employee.rank1}")
                        (context as EvaluationUpEmployeeActivity).startActivityForResult(intent, position)
                    }
                    EVALUATION_DOWN -> {
                        val intent=Intent(context, EvaluationDownActivity::class.java)
                        intent.putExtra("name",employee.fullName)
                        intent.putExtra("username",employee.username)
                        intent.putExtra("rank",employee.rank1)
                        (context as EvaluationDownEmployeeActivity).startActivityForResult(intent, position)
                    }
                    EVALUATION_SAME -> {
                        val intent=Intent(context, EvaluationSameActivity::class.java)
                        intent.putExtra("name",employee.fullName)
                        intent.putExtra("username",employee.username)
                        intent.putExtra("rank",employee.rank1)
                        (context as EvaluationSameEmployeeActivity).startActivityForResult(intent, position)
                    }
                    EXAM_DOWN -> {
                        val intent=Intent(context, ExamDownActivity::class.java)
                        intent.putExtra("name",employee.fullName)
                        intent.putExtra("username",employee.username)
                        (context as ExamDownEmployeeActivity).startActivityForResult(intent, position)
                    }
                }
            }
        }else{
            holder.itemView.setOnClickListener {
                //employeeList[position].evaluated=false
                when (type) {
                    EVALUATION_UP -> {
                        val intent=Intent(context,EvaluationUpActivity::class.java)
                        intent.putExtra("name",employee.fullName)
                        intent.putExtra("username",employee.username)
                        intent.putExtra("rank",employee.rank1)
                        Log.d("level:","${employee.rank1}")
                        (context as EvaluationUpEmployeeActivity).startActivityForResult(intent, position)
                    }
                    EVALUATION_DOWN -> {
                        val intent=Intent(context, EvaluationDownActivity::class.java)
                        intent.putExtra("name",employee.fullName)
                        intent.putExtra("username",employee.username)
                        intent.putExtra("rank",employee.rank1)
                        Log.d("level:","${employee.rank1}")
                        (context as EvaluationDownEmployeeActivity).startActivityForResult(intent, position)
                    }
                    EVALUATION_SAME -> {
                        val intent=Intent(context, EvaluationSameActivity::class.java)
                        intent.putExtra("name",employee.fullName)
                        intent.putExtra("username",employee.username)
                        intent.putExtra("rank",employee.rank1)
                        Log.d("level:","${employee.rank1}")
                        (context as EvaluationSameEmployeeActivity).startActivityForResult(intent, position)
                    }
                    EXAM_DOWN -> {
                        val intent=Intent(context, ExamDownActivity::class.java)
                        intent.putExtra("name",employee.fullName)
                        intent.putExtra("username",employee.username)
                        (context as ExamDownEmployeeActivity).startActivityForResult(intent, position)
                    }
                }
            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(View.inflate(parent.context, R.layout.item_employee, null))
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    private fun displayImage(imagePath: String, ava:ImageView) {
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

}