package com.example.evaluation.adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.example.evaluation.R
import com.example.evaluation.activity.EvaluationSameActivity
import com.example.evaluation.logic.EvaluationTitle
import com.example.evaluation.logic.QuestionScorePairs
import com.example.evaluation.utils.MyApplication

class EvaluationTitleAdapter(private val evaluationTitleList:ArrayList<EvaluationTitle>,private val context: Context,private val questionScorePairs: ArrayList<Int>):RecyclerView.Adapter<EvaluationTitleAdapter.ViewHolder>(){
    private val sItems = arrayOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "10")
    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val title: TextView =view.findViewById(R.id.evaluation_title)
        val score: TextView =view.findViewById(R.id.evaluation_score)
    }
    override fun getItemCount()=evaluationTitleList.size


    override fun onBindViewHolder(holder: EvaluationTitleAdapter.ViewHolder, position: Int) {
        var mCheckedItem:Int=0
        var flag=0
        val title=evaluationTitleList[position]
        val scores = Array(title.evaluationScore + 1) { "" }
        for (i in 0..title.evaluationScore){
            scores[i]=(title.evaluationScore-i).toString()
        }
        holder.title.text=title.evaluationQuestion
        holder.score.setOnClickListener {
            val builder = AlertDialog.Builder(context)
            builder.setTitle("请选择分数")
            builder.setSingleChoiceItems(scores, mCheckedItem) { dialog, which ->
                mCheckedItem = which
            }
            builder.setPositiveButton("确定") { dialog, which ->
                holder.score.text = scores[mCheckedItem]
                if(flag!=0){
                    questionScorePairs[position]=scores[mCheckedItem].toInt()
                }else{
                    questionScorePairs.add(scores[mCheckedItem].toInt())
                    flag++
                }

            }
            builder.setNegativeButton("取消") { dialog, which -> }
            builder.show()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EvaluationTitleAdapter.ViewHolder {
        val view=View.inflate(parent.context, R.layout.item_evaluation,null)
        return ViewHolder(view)
    }
}