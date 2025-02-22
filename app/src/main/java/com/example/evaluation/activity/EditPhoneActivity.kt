package com.example.evaluation.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.evaluation.R
import com.example.evaluation.databinding.ActivityEditPhoneBinding
import com.example.evaluation.logic.LoginResponse
import com.example.evaluation.logic.network.EvaluationNetwork
import com.example.evaluation.utils.MyApplication
import com.example.evaluation.utils.OnMultiClickListener
import com.example.evaluation.utils.SharePrefs
import com.example.evaluation.utils.showToast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EditPhoneActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditPhoneBinding
    private val sharePrefs= SharePrefs()
    private var token:String=sharePrefs.getToken(MyApplication.context,"token").toString()
    private lateinit var mCountDownTimerUtils:CountDownTimerUtils
    private var flag=0
    private var s=0
    private var username=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityEditPhoneBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setAndroidNativeLightStatusBar(this, true)
        /*binding.getCode.setOnClickListener {
            if(binding.oldTelNumber.text.toString().length!=11){
                binding.oldTelNumber.error="请输入正确的手机号码"
            }
            else{
                val intent= Intent(this, PuzzleActivity::class.java)
                intent.putExtra("oldTel",binding.oldTelNumber.text.toString())
                intent.putExtra("from",2)
                startActivityForResult(intent,1)
            }
        }
        mCountDownTimerUtils=CountDownTimerUtils(binding.getCode,60000,1000)*/
        username= intent.getStringExtra("name").toString()
        binding.confirm.setOnClickListener (object : OnMultiClickListener() {
            override fun onMultiClick(v: View){
                    CoroutineScope(Dispatchers.Main).launch {
                        bindPhone()
                    }
            }
        })

    }
    private fun setAndroidNativeLightStatusBar(activity: Activity, dark: Boolean) {
        val decor = activity.window.decorView
        if (dark) {
            decor.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)
        } else {
            decor.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE)
        }
    }

    /*override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode== Activity.RESULT_OK){
            binding.oldTelNumber.setText(data?.getStringExtra("oldTel"))
            CoroutineScope(Dispatchers.Main).launch {
                sendCode()
            }
        }
    }*/
    /*private suspend fun sendCode(){
        try {
            val response: LoginResponse = EvaluationNetwork.sendSMS(token, binding.oldTelNumber.text.toString())
            if(response.code!=200){
                CoroutineScope(Dispatchers.Main).launch {
                    response.message.showToast()
                }
            }
            if(response.code==200){
                mCountDownTimerUtils.start()
            }
            mCountDownTimerUtils.start()
        }catch(e: Exception) {
            Log.e("TAG", "InformationActivity: ", e)
        }
    }*/
    /*private suspend fun verify(){
        try {
            val response: LoginResponse = EvaluationNetwork.verifyCode(token, binding.oldTelNumber.text.toString(),binding.code.text.toString())
            if(response.code!=200){
                CoroutineScope(Dispatchers.Main).launch {
                    response.message.showToast()
                }
            }
            if(response.code==200 ){
            CoroutineScope(Dispatchers.Main).launch {
                    "换绑成功".showToast()
                }
                val intent= Intent(this,EditProfileActivity::class.java)
                startActivity(intent)
                finish()
            }

        }catch(e: Exception) {
            Log.e("TAG", "InformationActivity: ", e)
        }
    }*/
    inner class CountDownTimerUtils(
        private val mTextView: Button,
        millisInFuture: Long,
        countDownInterval: Long
    ) :
        CountDownTimer(millisInFuture, countDownInterval) {
        override fun onTick(millisUntilFinished: Long) {
            mTextView.isClickable = false //设置不可点击
            mTextView.text = (millisUntilFinished/1000).toString() + "秒后可重新发送" //设置倒计时时间
            mTextView.setBackgroundResource(R.color.black)
            mTextView.setBackgroundResource(R.drawable.new_btn_circle)//设置按钮为灰色，这时是不能点击的
            val spannableString = SpannableString(mTextView.text.toString()) //获取按钮上的文字
            val span = ForegroundColorSpan(Color.RED)
            spannableString.setSpan(span, 0, 2, Spannable.SPAN_INCLUSIVE_EXCLUSIVE) //将倒计时的时间设置为红色
            mTextView.text = spannableString
        }

        override fun onFinish() {
            mTextView.text = "重新获取"
            mTextView.isClickable = true //重新获得点击
        }
    }
    private suspend fun bindPhone(){
        try {
            val response: LoginResponse = EvaluationNetwork.changePhone(token, binding.oldTelNumber.text.toString(),binding.code.text.toString())
            CoroutineScope(Dispatchers.Main).launch {
                response.message.showToast()
            }
            if(response.code==200){
                CoroutineScope(Dispatchers.Main).launch {
                    "换绑成功".showToast()
                }
                val intent= Intent(this,EditProfileActivity::class.java)
                intent.putExtra("name",username)
                startActivity(intent)
                finish()
            }
        }catch(e: Exception) {
            Log.e("TAG", "InformationActivity: ", e)
        }
    }
}