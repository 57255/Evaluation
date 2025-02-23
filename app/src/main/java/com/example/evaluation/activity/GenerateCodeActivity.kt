package com.example.evaluation.activity

import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.evaluation.R
import com.example.evaluation.utils.CodeUtils

class GenerateCodeActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var codeUtils: CodeUtils
    private lateinit var txtCode: TextView
    private lateinit var imgCode: ImageView
    private lateinit var btCommit: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_generate_code)
        setBackground()
        init()
    }

    private fun init() {
        txtCode = findViewById(R.id.txt_code)
        imgCode = findViewById(R.id.img_code)
        btCommit = findViewById(R.id.bt_commit)
        // 设置验证码图片
        codeUtils = CodeUtils.getInstance()
        val bitmap = codeUtils.createBitmap()
        imgCode.setImageBitmap(bitmap)
        // 监听提交按钮与验证码图片
        btCommit.setOnClickListener(this)
        imgCode.setOnClickListener(this)
    }

    private fun setBackground() {
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
    }

    override fun onClick(view: View) {
        when (view.id) {
            R.id.img_code -> {   // 点击验证码图片时更新验证码
                val bitmap = codeUtils.createBitmap()
                val code = codeUtils.getCode()
                Log.i("验证码", code)
                imgCode.setImageBitmap(bitmap)
            }
            R.id.bt_commit -> {  // 当出现验证码为空或者验证码错误时要及时更新验证码(防止机器暴力破解口令)
                val inputCode = txtCode.text.toString()
                if (TextUtils.isEmpty(inputCode)) {  // 核查验证码是否为空
                    Toast.makeText(this, "验证码不能为空", Toast.LENGTH_SHORT).show()
                    // 设置验证码动态变化
                    val bitmap = codeUtils.createBitmap()
                    imgCode.setImageBitmap(bitmap)
                } else {
                    val code = codeUtils.getCode()
                    if (inputCode == code) { // 查验输入的验证码是否正确
                        Toast.makeText(this, "验证码正确", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this, LoginActivity::class.java)
                        setResult(RESULT_OK, intent)
                        finish()
                    } else {
                        Toast.makeText(this, "验证码不正确", Toast.LENGTH_SHORT).show()
                        // 设置验证码动态变化
                        val bitmap = codeUtils.createBitmap()
                        imgCode.setImageBitmap(bitmap)
                    }
                }
            }
        }
    }
}
