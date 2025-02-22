package com.example.evaluation.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.evaluation.databinding.ActivityEditProfileBinding
import com.example.evaluation.logic.GetEmployeeInformation
import com.example.evaluation.logic.network.EvaluationNetwork
import com.example.evaluation.utils.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EditProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditProfileBinding
    private val sharePrefs= SharePrefs()
    private var token:String=sharePrefs.getToken(MyApplication.context,"token").toString()
    private var name:String=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setAndroidNativeLightStatusBar(this, true)
        name=intent.getStringExtra("name").toString()
        CoroutineScope(Dispatchers.Main).launch {
                    getInfo()
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
    private fun displayImage(imagePath: String) {
        if (!TextUtils.isEmpty(imagePath)) {
            //显示图片
            Glide.with(this).load(imagePath).apply(requestOptions).into(binding.camera)

            //压缩图片
          /*  orc_bitmap = CameraUtils.compression(BitmapFactory.decodeFile(imagePath))!!
            //转Base64
            base64Pic = BitmapUtils.bitmapToBase64(orc_bitmap)*/
        } else {
            ("图片获取失败").showToast()
        }
    }
    private lateinit var base64Pic: String

    //拍照和相册获取图片的Bitmap
    private lateinit var orc_bitmap: Bitmap

    //Glide请求图片选项配置
    private val requestOptions = RequestOptions.circleCropTransform()
        .diskCacheStrategy(DiskCacheStrategy.NONE) //不做磁盘缓存
        .skipMemoryCache(true) //不做内存缓存
    private suspend fun getInfo(){
        try {
            val response: GetEmployeeInformation = EvaluationNetwork.getInfo(token,name
            )
            if(response.code!=200){
                CoroutineScope(Dispatchers.Main).launch {
                    response.message.showToast()
                }
            }
            if(response.code==200){
                binding.infoFullName.text=response.data.fullName
                binding.infoTelephone.text=response.data.phone
                binding.infoEmployeeId.text=response.data.username
                binding.infoEmploy.text= response.data.rank1
                binding.infoEmp.text=response.data.position
                binding.infodep.text=response.data.department
                displayImage("http://110.41.60.28:81/avatar/"+response.data.avatar)
            }
        }catch(e: Exception) {
            Log.e("TAG", "InformationActivity: ", e)
        }
    }
}