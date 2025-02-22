package com.example.evaluation.puzzle

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Bitmap
import android.util.AttributeSet
import android.view.View
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.animation.doOnEnd
import com.example.evaluation.R
import java.lang.Math.abs

class SlidePuzzle : ConstraintLayout {

    private lateinit var mTipText : TextView
    private lateinit var mPuzzle: Puzzle
    private lateinit var mSlideBar: SlideBar

    private var onVerify : ((Boolean) -> Unit)? = null

    fun setOnVerifyListener(listener : (Boolean) -> Unit) {
        onVerify = listener
    }

    fun setBitmap(bitmap: Bitmap) {
        mPuzzle.setBitmap(bitmap)
        mSlideBar.setOnDragListener { progress, useTime, verify ->
            //同步滑块位置
            mPuzzle.setProgress(progress)
            //停止滑动时验证
            if(verify) { verify(abs(progress * 0.9f - mPuzzle.getCurRandomX()) < 0.018f,useTime) }
        }
    }

    private fun verify(isSuccess : Boolean,useTime : Float?) {
        mTipText.text = if(isSuccess && useTime != null) {
            String.format("拼图成功: 耗时%.1f秒,打败了%d%%的用户!",useTime,(99 - ((if (useTime > 1f) useTime - 1f else 0f) / 0.1f)).toInt())
        }else {
            "拼图失败: 请重新拖动"
        }
        mTipText.visibility = View.VISIBLE
        ValueAnimator.ofFloat(0.8f,0.72f).apply {
            addUpdateListener { mTipText.translationY = it.animatedValue as Float }
            duration = 1500
            start()
            doOnEnd { mTipText.visibility = View.GONE }
        }
        if(isSuccess) {
            mPuzzle.showSuccessAnim()
            onVerify?.invoke(true)
        } else {
            mSlideBar.reset()
            onVerify?.invoke(false)
        }
    }

    private fun initView() {
        val view=View.inflate(context, R.layout.layout_slide_puzzle,this)
        mPuzzle=view.findViewById(R.id.puzzle)
        mSlideBar=view.findViewById(R.id.slide_bar)
        mTipText=view.findViewById(R.id.tips_text)
    }

    constructor(context: Context) : super(context) { initView() }

    constructor(context: Context, attributeSet: AttributeSet) : super(context,attributeSet) { initView() }

    constructor(context: Context, attributeSet: AttributeSet, defStyle : Int) : super(context,attributeSet,defStyle) { initView() }
}