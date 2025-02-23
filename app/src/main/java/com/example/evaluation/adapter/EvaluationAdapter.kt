package com.example.evaluation.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.evaluation.R
import com.example.evaluation.logic.Evaluation
import com.example.evaluation.utils.BitmapUtils
import com.example.evaluation.utils.CameraUtils


class EvaluationAdapter(private val evaluationList:ArrayList<Evaluation>,private val context: Context) :RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    inner class ViewHolder1(view: View): RecyclerView.ViewHolder(view){
        val ava: ImageView =view.findViewById(R.id.ranking_first_ava)
        val name: TextView =view.findViewById(R.id.ranking_first_employee)
        val score: TextView =view.findViewById(R.id.ranking_first_score)
    }
    inner class ViewHolder2(view: View): RecyclerView.ViewHolder(view){
        val ava: ImageView =view.findViewById(R.id.ranking_second_ava)
        val name: TextView =view.findViewById(R.id.ranking_second_employee)
        val score: TextView =view.findViewById(R.id.ranking_second_score)
    }
    inner class ViewHolder3(view: View): RecyclerView.ViewHolder(view){
        val ava: ImageView =view.findViewById(R.id.ranking_third_ava)
        val name: TextView =view.findViewById(R.id.ranking_third_employee)
        val score: TextView =view.findViewById(R.id.ranking_third_score)
    }
    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val ava: ImageView =view.findViewById(R.id.ranking_ava)
        val name: TextView =view.findViewById(R.id.ranking_employee)
        val score: TextView =view.findViewById(R.id.ranking_score)
        val number: TextView =view.findViewById(R.id.rankNumber)
    }
    override fun getItemCount()=evaluationList.size
    private lateinit var ava:ImageView
    private lateinit var onLoadMoreListener: OnLoadMoreListener
    interface OnLoadMoreListener{
        fun onLoadMore()
    }

    fun setOnLoadMoreListener(onLoadMoreListener: OnLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener
    }
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val evaluation=evaluationList[position]
        if(position==evaluationList.size-1){
            holder.itemView.post {
                onLoadMoreListener.onLoadMore()
                Log.d("EvaluationAdapter", "onBindViewHolder:加载更多")
                Log.d("EvaluationAdapter", "onBindViewHolder:${evaluationList.size}")
            }
        }
        when (position) {
            0 -> {
                val holder=holder as ViewHolder1
                if(evaluation.fullName==null){
                    holder.name.text="电厂员工"
                }else{
                    holder.name.text=evaluation.fullName
                }
                holder.score.text=evaluation.finalScore.toString()
                ava=holder.ava
                /*if(evaluation.avatar!=null&&evaluation.avatar!=""){
                    displayImage("http://110.41.60.28:81/avatar/"+evaluation.avatar,holder.ava)
                }*/
                displayImage("http://10.48.27.15:81/avatar/"+evaluation.avatar,holder.ava)
                Log.d("EvaluationAdapter", "onBindViewHolder:没问题")
                return
            }
            1 -> {
                val holder=holder as ViewHolder2
                if(evaluation.fullName==null){
                    holder.name.text="电厂员工"
                }else{
                    holder.name.text=evaluation.fullName
                }
                holder.score.text=evaluation.finalScore.toString()
                ava=holder.ava
                /*if(evaluation.avatar!=null&&evaluation.avatar!=""){
                    displayImage("http://110.41.60.28:81/avatar/"+evaluation.avatar,holder.ava)
                }*/
                displayImage("http://10.48.27.15:81/avatar/"+evaluation.avatar,holder.ava)
                Log.d("EvaluationAdapter", "onBindViewHolder:没问题")
                return
            }
            2 -> {
                val holder=holder as ViewHolder3
                if(evaluation.fullName==null&&evaluation.avatar!=""){
                    holder.name.text="电厂员工"
                }else{
                    holder.name.text=evaluation.fullName
                }
                holder.score.text=evaluation.finalScore.toString()
                ava=holder.ava
                /*if(evaluation.avatar!=null&&evaluation.avatar!=""){
                    displayImage("http://110.41.60.28:81/avatar/"+evaluation.avatar,holder.ava)
                }*/
                displayImage("http://10.48.27.15:81/avatar/"+evaluation.avatar,holder.ava)
                Log.d("EvaluationAdapter", "onBindViewHolder:没问题")
                return
            }
            else -> {
                val holder=holder as ViewHolder
                if(evaluation.fullName==null){
                    holder.name.text="电厂员工"
                }else{
                    holder.name.text=evaluation.fullName
                }
                holder.score.text=evaluation.finalScore.toString()
                holder.number.text=(position+1).toString()
                ava=holder.ava
                /*if(evaluation.avatar!=null&&evaluation.avatar!=""){
                    Log.d("EvaluationAdapter", "onBindViewHolder:${evaluation.avatar}")
                    displayImage("http://110.41.60.28:81/avatar/"+evaluation.avatar,holder.ava)

                }*/
                displayImage("http://10.48.27.15:81/avatar/"+evaluation.avatar,holder.ava)
            }
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) :RecyclerView.ViewHolder{
        when (viewType) {
            0 -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_first, parent, false)
                return ViewHolder1(view)
            }
            1 -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_second, parent, false)
                return ViewHolder2(view)
            }
            2 -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_third, parent, false)
                return ViewHolder3(view)
            }
            else -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_ranking, parent, false)
                return ViewHolder(view)
            }
        }

    }

    override fun getItemViewType(position: Int): Int {
        return position
    }

    private lateinit var base64Pic: String

    //拍照和相册获取图片的Bitmap
    private lateinit var orc_bitmap: Bitmap
    private val requestOptions = RequestOptions.circleCropTransform()
        .diskCacheStrategy(DiskCacheStrategy.NONE) //不做磁盘缓存
        .skipMemoryCache(true) //不做内存缓存
    private fun displayImage(imagePath: String,ava:ImageView) {
        if (!TextUtils.isEmpty(imagePath)) {
            //显示图片
            Glide.with(context).load(imagePath).apply(requestOptions).into(ava)

            //压缩图片
            /*orc_bitmap = CameraUtils.compression(BitmapFactory.decodeFile(imagePath))!!
            //转Base64
            base64Pic = BitmapUtils.bitmapToBase64(orc_bitmap)*/
        } else {
            /*("图片获取失败").showToast()*/
        }
    }
    fun insertEvaluation(evaluation: Evaluation){
        evaluationList.add(evaluation)
        notifyItemInserted(evaluationList.size)
    }
}
