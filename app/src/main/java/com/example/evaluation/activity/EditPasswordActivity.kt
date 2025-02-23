package com.example.evaluation.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.evaluation.databinding.ActivityEditPasswordBinding
import com.example.evaluation.logic.LoginResponse
import com.example.evaluation.logic.network.EvaluationNetwork
import com.example.evaluation.utils.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EditPasswordActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditPasswordBinding
    private val sharePrefs= SharePrefs()
    private var token:String=sharePrefs.getToken(MyApplication.context,"token").toString()
    private val prefs = MyApplication.context.getSharedPreferences("data", Context.MODE_PRIVATE)
    private val editor = prefs.edit()
    private val tel:Int=0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityEditPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setAndroidNativeLightStatusBar(this, true)
        binding.oldPassword.transformationMethod = PasswordTransformationMethod.getInstance()
        binding.newPassword.transformationMethod = PasswordTransformationMethod.getInstance()
        binding.confirmPassword.transformationMethod = PasswordTransformationMethod.getInstance()
        val intent=intent
        val tel=intent.getIntExtra("tel",0)
        binding.editPasswordBtn.setOnClickListener (object : OnMultiClickListener() {
            override fun onMultiClick(v: View){
                CoroutineScope(Dispatchers.Main).launch {
                    fix()
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
    private suspend fun fix(){
        try{
            val oldPassword=binding.oldPassword.text.toString()
            val newPassword=binding.newPassword.text.toString()
            val okPassword=binding.confirmPassword.text.toString()
            Log.d("TAG", "fix: $oldPassword")
            Log.d("TAG", "fix: $newPassword")
            Log.d("TAG", "fix: $okPassword")
            if(newPassword!=okPassword){
                "两次密码不一致".showToast()
                return
            }
            val fixResponse: LoginResponse = EvaluationNetwork.changePassword(token,oldPassword,newPassword)
            Log.d("fixResponse.code", fixResponse.code.toString())
            if(fixResponse.code==200){
                "修改成功".showToast()
                if(tel==1){
                    val intent= Intent(this, EditRankActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                else{
                    val intent= Intent(this, LoginActivity::class.java)
                    editor.putString("name", "")
                    editor.putString("password", "")
                    startActivity(intent)
                    finish()
                }
            }else{
                fixResponse.message.showToast()
            }
        }catch(e: Exception) {
            Log.e("TAG", "passwordEditActivity: ", e)
        }
    }
}