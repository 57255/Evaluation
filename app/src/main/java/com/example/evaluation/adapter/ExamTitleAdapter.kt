package com.example.evaluation.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.evaluation.R
import com.example.evaluation.logic.Question
import com.example.evaluation.utils.MyApplication

class ExamTitleAdapter(private val examTitleList:ArrayList<Question>,private val context: Context,private val questionScorePairs: ArrayList<Map<String,Int>>): RecyclerView.Adapter<ExamTitleAdapter.ViewHolder>(){
    val sItems = arrayOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20")

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val title: TextView =view.findViewById(R.id.exam_title)
        val score: TextView =view.findViewById(R.id.exam_score)
    }
    override fun getItemCount()=examTitleList.size
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var mCheckedItem:Int=0
        var flag=0

        val title=examTitleList[position].question
        val scores = Array(examTitleList[position].score + 1) { "" }
        holder.title.text=title
        for(i in 0..examTitleList[position].score){
            scores[i]=(examTitleList[position].score-i).toString()
        }
        holder.score.setOnClickListener {
            val builder = AlertDialog.Builder(context)
            builder.setTitle("请选择分数")
            builder.setSingleChoiceItems(scores, mCheckedItem) { dialog, which ->
                mCheckedItem = which
            }
            builder.setPositiveButton("确定") { dialog, which ->
                holder.score.text = scores[mCheckedItem]
                if(flag!=0){
                    questionScorePairs[position]=mapOf(Pair(title,scores[mCheckedItem].toInt()))
                }else{
                    questionScorePairs.add(mapOf(Pair(title,scores[mCheckedItem].toInt())))
                    flag++
                }
            }
            builder.setNegativeButton("取消") { dialog, which -> }
            builder.show()
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view=View.inflate(parent.context, R.layout.item_exam,null)
        return ViewHolder(view)
    }
}