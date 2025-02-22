package com.example.evaluation.adapter

import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.evaluation.R


class NoticeAdapter(private val noticeList:ArrayList<String>):RecyclerView.Adapter<RecyclerView.ViewHolder>(){
    inner class ViewHolder(view: View):RecyclerView.ViewHolder(view){
        val notice: TextView =view.findViewById(R.id.notice)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(View.inflate(parent.context, R.layout.item_notice, null))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val holder=holder as ViewHolder
        val tempPos: Int = position % noticeList.size
        holder.notice.text = noticeList[tempPos]
    }

    override fun getItemCount(): Int {
        return 1000000
    }
}