package com.example.evaluation.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
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

class EvaluationDetailAdapter (private val evaluationList:ArrayList<Evaluation>, private val context: Context) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val ava: ImageView =view.findViewById(R.id.ranking_ava)
        val name: TextView =view.findViewById(R.id.ranking_employee)
        val score: TextView =view.findViewById(R.id.ranking_score)
        val number: TextView =view.findViewById(R.id.rankNumber)
    }
    override fun getItemCount()=evaluationList.size
    private lateinit var ava: ImageView
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
                val holder=holder as ViewHolder
                holder.name.text=evaluation.fullName
                holder.score.text=evaluation.finalScore.toString()
                holder.number.text=(position+1).toString()
                ava=holder.ava
                /*if(evaluation.avatar!=null&&evaluation.avatar!=""){
                    Log.d("EvaluationAdapter", "onBindViewHolder:${evaluation.avatar}")
                    displayImage("http://10.48.27.15:81/avatar/"+evaluation.avatar,holder.ava)
        }*/
        displayImage("http://10.48.27.15:8801/avatar/"+evaluation.avatar,holder.ava)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) : RecyclerView.ViewHolder{
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_ranking, parent, false)
                return ViewHolder(view)
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
    private fun displayImage(imagePath: String,ava: ImageView) {
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