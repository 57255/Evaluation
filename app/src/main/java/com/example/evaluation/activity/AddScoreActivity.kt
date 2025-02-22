package com.example.evaluation.activity

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.evaluation.R
import com.example.evaluation.databinding.ActivityAddScoreBinding
import com.example.evaluation.logic.LoginResponse
import com.example.evaluation.logic.network.EvaluationNetwork
import com.example.evaluation.ui.dashboard.DashboardFragment
import com.example.evaluation.utils.*
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.tbruyelle.rxpermissions3.RxPermissions
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class AddScoreActivity : AppCompatActivity() {
    private lateinit var binding:ActivityAddScoreBinding
    private val sharePrefs= SharePrefs()
    private lateinit var outputImagePath: File
    private lateinit var bottomView: View
    private lateinit var rxPermissions: RxPermissions
    private lateinit var bottomSheetDialog: BottomSheetDialog
    private  var path:String=""
    var imagePath:String?=null
    private val prefs = MyApplication.context.getSharedPreferences("data", Context.MODE_PRIVATE)
    private val editor = prefs.edit()
    private var token:String=sharePrefs.getToken(MyApplication.context,"token").toString()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddScoreBinding.inflate(layoutInflater)
        editor.putBoolean("hasPermissions", false)
        editor.apply()
        setContentView(binding.root)
        setAndroidNativeLightStatusBar(this, true)
        bottomSheetDialog= BottomSheetDialog(this)
        bottomView = layoutInflater.inflate(R.layout.dialog_bottom, null)
        ivHead=binding.proof
        val tvTakePictures: TextView = bottomView.findViewById(R.id.tv_take_pictures)
        val tvOpenAlbum: TextView = bottomView.findViewById(R.id.tv_open_album)
        val tvCancel: TextView = bottomView.findViewById(R.id.tv_cancel)
        binding.proof.setOnClickListener {
            bottomSheetDialog.setContentView(bottomView)
            //拍照
            tvTakePictures.setOnClickListener{
                takePhoto()
                bottomSheetDialog.cancel()
            }
            //打开相册
            tvOpenAlbum.setOnClickListener {
                openAlbum()
                bottomSheetDialog.cancel()
            }
            //取消
            tvCancel.setOnClickListener{
                bottomSheetDialog.cancel()
            }
            bottomSheetDialog.show()
        }
        binding.addScoreBtn.setOnClickListener(object : OnMultiClickListener() {
            override fun onMultiClick(v: View) {
                // 在这里处理点击事件
                // 例如，增加分数
                if(binding.score.text.toString().isEmpty()){
                    binding.score.error="请输入分数"
                }
                if(binding.employeeNumber.text.toString().isEmpty()){
                    binding.employeeNumber.error="请输入员工工号"
                }
                if(binding.reason.text.toString().isEmpty()){
                    binding.reason.error="请输入原因"
                }else if(outputImagePath==null){
                    "请上传凭证".showToast()
                }else{
                    CoroutineScope(Dispatchers.Main).launch {
                        addProof()
                    }
                }
            }
        })

    }
    private fun checkVersionCamera() {
        //Android6.0及以上版本
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //如果你是在Fragment中，则把this换成getActivity()
            rxPermissions = RxPermissions(this)
            //权限请求
            rxPermissions.request(
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
                .subscribe { granted ->
                    editor.putBoolean("hasPermissions", true)
                    var timeStampFormat: SimpleDateFormat = SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
                    val filename:String  = timeStampFormat.format(Date())
                    outputImagePath= File("$filename.jpg")
                    val takePhotoIntent = Intent(CameraUtils.getTakePhotoIntent(MyApplication.context, outputImagePath))
                    // 开启一个带有返回值的Activity，请求码为TAKE_PHOTO
                    startActivityForResult(takePhotoIntent, TAKE_PHOTO)
                    if (granted) { //申请成功
                    } else { //申请失败
                        ("权限未开启").showToast()
                    }
                }
        } else {
            //Android6.0以下
            ("无需请求动态权限").showToast()
        }
    }
    private fun takePhoto() {
        val hasPermissions=prefs.getBoolean("hasPermissions",false)
        if (!hasPermissions) {
            checkVersionCamera()
            return
        }
    }

    /**
     * 打开相册
     */
    private fun openAlbum() {
        val hasPermissions=prefs.getBoolean("hasPermissions",false)
        if (!hasPermissions) {
            checkVersionAlbum()
            return
        }

    }
    private fun checkVersionAlbum() {
        //Android6.0及以上版本
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //如果你是在Fragment中，则把this换成getActivity()
            rxPermissions = RxPermissions(this)
            //权限请求
            rxPermissions.request(
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
                .subscribe { granted ->
                    if (granted) { //申请成功
                        editor.putBoolean("hasPermissions", true)
                        var timeStampFormat: SimpleDateFormat = SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
                        val filename:String  = timeStampFormat.format(Date())
                        outputImagePath= File("$filename.jpg")
                        startActivityForResult(CameraUtils.selectPhotoIntent, SELECT_PHOTO)
                    } else { //申请失败
                        ("权限未开启").showToast()
                    }
                }
        } else {
            //Android6.0以下
            ("无需请求动态权限").showToast()
        }
    }

    //图片控件

    //Base64
    private lateinit var base64Pic: String

    //拍照和相册获取图片的Bitmap
    private lateinit var orc_bitmap: Bitmap

    //Glide请求图片选项配置
    private val requestOptions = RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE) //不做磁盘缓存
        .skipMemoryCache(true) //不做内存缓存

    /**
     * 返回到Activity
     * @param requestCode
     * @param resultCode
     * @param data
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            TAKE_PHOTO -> if (resultCode == Activity.RESULT_OK) {
                //显示图片
                  displayImage(outputImagePath.toString())
            }
            SELECT_PHOTO -> if (resultCode == Activity.RESULT_OK) {
                //判断手机系统版本号
                imagePath= if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
                    //4.4及以上系统使用这个方法处理图片
                    data?.let { CameraUtils.getImageOnKitKatPath(it, MyApplication.context) }
                } else {
                    data?.let { CameraUtils.getImageBeforeKitKatPath(it, MyApplication.context) }
                }
                //显示图片
                Log.d("dhasjvgbfcuikjasbcav",imagePath.toString())
                outputImagePath= File(imagePath.toString())
                displayImage(outputImagePath.toString())
            }
            else -> {}
        }
    }

    private lateinit var ivHead:ImageView
    /**
     * 通过图片路径显示图片
     */
    private fun displayImage(imagePath: String) {
        if (!TextUtils.isEmpty(imagePath)) {
            //显示图片
            Glide.with(this).load(imagePath).apply(requestOptions).into(ivHead)

            //压缩图片
            /*orc_bitmap = CameraUtils.compression(BitmapFactory.decodeFile(imagePath))!!
            //转Base64
            base64Pic = BitmapUtils.bitmapToBase64(orc_bitmap)*/
        } else {
            ("图片获取失败").showToast()
        }
    }
    private suspend fun addProof(){
        try {
            val response: LoginResponse = EvaluationNetwork.addBonusItem(token, PERFORMANCE_RECORD,binding.reason.text.toString(),binding.score.text.toString().toInt(),binding.employeeNumber.text.toString(),outputImagePath)
            if(response.code!=200){
                CoroutineScope(Dispatchers.Main).launch {
                    response.message.showToast()
                }
            }
            if(response.code==200 ){
                CoroutineScope(Dispatchers.Main).launch {
                    "提交成功".showToast()
                }
                val intent= Intent(this,DashboardFragment::class.java)
                startActivity(intent)
                finish()
            }

        }catch(e: Exception) {
            Log.e("TAG", "InformationActivity: ", e)
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
}