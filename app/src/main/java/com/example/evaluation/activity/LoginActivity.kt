package com.example.evaluation.activity

import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.evaluation.MainActivity
import com.example.evaluation.databinding.ActivityLoginBinding
import com.example.evaluation.logic.GetEmployeeInformation
import com.example.evaluation.logic.Login
import com.example.evaluation.logic.LoginResponse
import com.example.evaluation.logic.network.EvaluationNetwork
import com.example.evaluation.utils.MyApplication.Companion.context
import com.example.evaluation.utils.RANK
import com.example.evaluation.utils.showToast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.math.log


class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setAndroidNativeLightStatusBar(this, true)

        val prefs = getSharedPreferences("data", Context.MODE_PRIVATE)
        val isRemember = prefs.getBoolean("remember_password", false)
        if (isRemember) {
            binding.CardNumber.setText(prefs.getString("name", ""))
            binding.Password.setText(prefs.getString("password", ""))
            binding.rememberPassword.isChecked = true
            binding.Password.apply {
                transformationMethod = PasswordTransformationMethod.getInstance()
            }
        }
        binding.login.setOnClickListener {
            val intent = Intent(this,GenerateCodeActivity::class.java)
            startActivityForResult(intent, 1)
        }
        binding.inercut.setOnClickListener {
            val intent=Intent(this, ForgetActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
    private fun setAndroidNativeLightStatusBar(activity: Activity, dark: Boolean) {
        val decor = activity.window.decorView
        if (dark) {
            decor.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)
        } else {
            decor.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE)
        }
    }
    private suspend fun login() {
        try {
            val name = binding.CardNumber.text.toString()
            val password = binding.Password.text.toString()
            val response: LoginResponse = EvaluationNetwork.login(Login(name,password))

            CoroutineScope(Dispatchers.Main).launch {
                Toast.makeText(this@LoginActivity, response.message, Toast.LENGTH_SHORT).show()
            }
            if (response.code == 200) {
                val token=response.data
                val prefs = getSharedPreferences("data",Context.MODE_PRIVATE)
                val editor = prefs.edit()
                editor.putString("token",token)
                editor.putString("name", name)
                if (binding.rememberPassword.isChecked) {
                    editor.putBoolean("remember_password", true)
                    editor.putString("name", name)
                    editor.putString("password", password)
                } else {
                    editor.clear()
                }
                editor.apply()
                CoroutineScope(Dispatchers.Main).launch {
                   getInfo(token)
                }
            } else if(response.code==201){
                CoroutineScope(Dispatchers.Main).launch {
                    response.message.showToast()
                }
                val intent=Intent(this,EditPasswordActivity::class.java)
                startActivity(intent)
                finish()
            }
        } catch (e: Exception) {
            Log.e("TAG", "login(): ", e)
        }
    }
    private suspend fun getInfo(token:String){
        try {
            val response: GetEmployeeInformation = EvaluationNetwork.getInfo(token,binding.CardNumber.text.toString())
            if(response.code==200){
                RANK=response.data.rank1
                if(response.data.phone==null){
                    val intent=Intent(this,EditRankActivity::class.java)
                    startActivity(intent)
                    finish()
                }else{
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
            else {
                CoroutineScope(Dispatchers.Main).launch {
                    response.message.showToast()
                }
            }
        }catch(e: Exception) {
            "网络错误".showToast()
            Log.e("wQHfHX", "InformationActivity: ", e)
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.d("LoginActivity", "onActivityResult called with requestCode: $requestCode, resultCode: $resultCode")
        if (resultCode == RESULT_OK) {
            Log.d("request","ok")
            CoroutineScope(Dispatchers.IO).launch {
                login()
            }
        }
    }
}