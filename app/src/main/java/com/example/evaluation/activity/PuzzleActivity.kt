package com.example.evaluation.activity

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.evaluation.R
import com.example.evaluation.puzzle.SlidePuzzle

class PuzzleActivity : AppCompatActivity() {
    private var from=0
    private var firstTel=""
    private var oldTel=""
    private var thirdTel=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_puzzle)
        val intent = intent
        from = intent.getIntExtra("from", 0)
        firstTel = intent.getStringExtra("firstTel").toString()
        oldTel = intent.getStringExtra("oldTel").toString()
        thirdTel = intent.getStringExtra("thirdTel").toString()
        (findViewById<View>(R.id.slide_puzzle) as SlidePuzzle).setBitmap(
            BitmapFactory.decodeResource(
                resources, R.drawable.ic_slide_puzzle_test
            )
        )
        (findViewById<View>(R.id.slide_puzzle) as SlidePuzzle).setOnVerifyListener { aBoolean: Boolean ->
            Toast.makeText(
                this@PuzzleActivity,
                if (aBoolean) "验证成功"

                else "验证失败",
                Toast.LENGTH_SHORT
            ).show()
            if(aBoolean&&from==1){
                val intent= Intent(this, PhoneActivity::class.java)
                intent.putExtra("firstTel",firstTel)
                setResult(RESULT_OK,intent)
                finish()
            }
            if(aBoolean&&from==2){
                val intent= Intent(this, EditPhoneActivity::class.java)
                intent.putExtra("oldTel",oldTel)
                setResult(RESULT_OK,intent)
                finish()
            }
            if(aBoolean&&from==3){
                val intent= Intent(this, TestPhoneActivity::class.java)
                intent.putExtra("thirdTel",thirdTel)
                setResult(RESULT_OK,intent)
                finish()
            }
        }
    }
}