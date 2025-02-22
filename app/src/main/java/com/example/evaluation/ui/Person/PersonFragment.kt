package com.example.evaluation.ui.Person

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.example.evaluation.R
import com.example.evaluation.activity.*
import com.example.evaluation.databinding.FragmentPersonBinding
import com.example.evaluation.logic.Evaluation
import com.example.evaluation.logic.GetAvaterResponse
import com.example.evaluation.logic.GetEmployeeInformation
import com.example.evaluation.logic.LoginResponse
import com.example.evaluation.logic.network.EvaluationNetwork
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


class PersonFragment : Fragment() {

    private var _binding: FragmentPersonBinding? = null
    private val binding get() = _binding!!
    private lateinit var outputImagePath:File
    private lateinit var bottomView:View
    private lateinit var rxPermissions: RxPermissions
    private lateinit var bottomSheetDialog: BottomSheetDialog
    private  var path:String=""
    private var hasPermissions=false
    private var username=""
    private val prefs = MyApplication.context.getSharedPreferences("data", Context.MODE_PRIVATE)
    private val editor = prefs.edit()
    private val sharePrefs= SharePrefs()
    private var token:String=sharePrefs.getToken(MyApplication.context,"token").toString()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPersonBinding.inflate(inflater, container, false)
        editor.putBoolean("hasPermissions", false)
        editor.apply()
        return binding.root    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bottomSheetDialog= activity?.let { BottomSheetDialog(it) }!!
        bottomView = layoutInflater.inflate(R.layout.dialog_bottom, null)
        ivHead=binding.ava
        val tvTakePictures: TextView = bottomView.findViewById(R.id.tv_take_pictures)
        val tvOpenAlbum: TextView = bottomView.findViewById(R.id.tv_open_album)
        val tvCancel: TextView = bottomView.findViewById(R.id.tv_cancel)
        CoroutineScope(Dispatchers.Main).launch {
                    getAva(token)
                }
        CoroutineScope(Dispatchers.Main).launch {
                    getInfo()
                }

        binding.ava.setOnClickListener {
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

        binding.profileCard.setOnClickListener {
            val intent=Intent(requireContext(),EditProfileActivity::class.java)
            intent.putExtra("avater",path)
            intent.putExtra("name",binding.card.text)
            startActivity(intent)

        }
        binding.descriptionProfile.setOnClickListener {
            val intent=Intent(requireContext(),EditProfileActivity::class.java)
            intent.putExtra("avater",path)
            intent.putExtra("name",binding.card.text)
            startActivity(intent)
        }
        binding.rightProfile.setOnClickListener {
            val intent=Intent(requireContext(),EditProfileActivity::class.java)
            intent.putExtra("avater",path)
            intent.putExtra("name",binding.card.text)
            startActivity(intent)
        }
        binding.textProfile.setOnClickListener {
            val intent=Intent(requireContext(),EditProfileActivity::class.java)
            intent.putExtra("avater",path)
            intent.putExtra("name",binding.card.text)
            startActivity(intent)
        }
        binding.descriptionPassword.setOnClickListener {
            val intent=Intent(requireContext(), EditPasswordActivity::class.java)
            startActivity(intent)
        }
        binding.textPassword.setOnClickListener {
            val intent=Intent(requireContext(), EditPasswordActivity::class.java)
            startActivity(intent)
        }
        binding.rightPassword.setOnClickListener {
            val intent=Intent(requireContext(), EditPasswordActivity::class.java)
            startActivity(intent)
        }
        binding.passwordCard.setOnClickListener {
            val intent=Intent(requireContext(), EditPasswordActivity::class.java)
            startActivity(intent)
        }
        binding.textTel.setOnClickListener {
            val intent=Intent(requireContext(), EditPhoneActivity::class.java)
            intent.putExtra("name",username)
            startActivity(intent)
        }
        binding.rightTel.setOnClickListener {
            val intent=Intent(requireContext(), EditPhoneActivity::class.java)
            intent.putExtra("name",username)
            startActivity(intent)
        }
        binding.descriptionTel.setOnClickListener {
            val intent=Intent(requireContext(), EditPhoneActivity::class.java)
            intent.putExtra("name",username)
            startActivity(intent)
        }
        binding.telCard.setOnClickListener {
            val intent=Intent(requireContext(), EditPhoneActivity::class.java)
            intent.putExtra("name",username)
            startActivity(intent)
        }
        binding.scoreCard.setOnClickListener {
            val intent=Intent(requireContext(), ScoreDetailActivity::class.java)
            startActivity(intent)
        }
        binding.logout.setOnClickListener {
            val intent=Intent(requireContext(), LoginActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
        }

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
            rxPermissions = activity?.let { RxPermissions(it) }!!
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
    private val requestOptions = RequestOptions.circleCropTransform()
        .diskCacheStrategy(DiskCacheStrategy.NONE) //不做磁盘缓存
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
                CoroutineScope(Dispatchers.Main).launch {
                    setAva(token,outputImagePath)
                }
            }
            SELECT_PHOTO -> if (resultCode == Activity.RESULT_OK) {
                var imagePath:String?=null//判断手机系统版本号
                imagePath= if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
                    //4.4及以上系统使用这个方法处理图片
                    data?.let { CameraUtils.getImageOnKitKatPath(it, MyApplication.context) }
                } else {
                    data?.let { CameraUtils.getImageBeforeKitKatPath(it, MyApplication.context) }
                }
                //显示图片
                Log.d("dhasjvgbfcuikjasbcav",imagePath.toString())
                CoroutineScope(Dispatchers.Main).launch {
                    setAva(token,File(imagePath.toString()))
                }
            }
            else -> {}
        }
    }

    private lateinit var ivHead: CircleImageView
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
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    private suspend fun getInfo(){
        try {
            val response: GetEmployeeInformation = EvaluationNetwork.getInfo(token,
                prefs.getString("name","")!!
            )
            Log.d("TAG", "InformationActivity: "+prefs.getString("name","")!!)
            if(response.code!=200){
                CoroutineScope(Dispatchers.Main).launch {
                    Toast.makeText(requireContext(), "获取信息失败", Toast.LENGTH_SHORT).show()
                }
            }
            if(response.code==200){
                binding.name.text= response.data.fullName
                binding.card.text= response.data.username
                username=response.data.username
            }
        }catch(e: Exception) {
            Log.e("TAG", "InformationActivity: ", e)
        }
    }
    private suspend fun getAva(token: String){
        try {
            val response: GetAvaterResponse = EvaluationNetwork.getAvater(token)
            if(response.code!=200){
                CoroutineScope(Dispatchers.Main).launch {
                    response.message.showToast()
                }
            }
            if(response.code==200){
                if(response.data!=null){
                    displayImage("http://110.41.60.28:81/avatar/"+response.data)
                    path=response.data
                }
            }
        }catch(e: Exception) {
            Log.e("TAG", "InformationActivity: ", e)
        }
    }
    private suspend fun setAva(token: String, file: File){
        try {
            val response:LoginResponse = EvaluationNetwork.setAvater(token,file)
            if(response.code!=200){
                CoroutineScope(Dispatchers.Main).launch {
                    response.message.showToast()
                }
            }
            if(response.code==200){
                displayImage(file.absolutePath)
            }
        }catch(e: Exception) {
            Log.e("TAG", "InformationActivity: ", e)
        }
    }
    private suspend fun logout(token: String){
        try {
            val response:LoginResponse = EvaluationNetwork.logout(token)
            if(response.code!=200){
                CoroutineScope(Dispatchers.Main).launch {
                    response.message.showToast()
                }
            }
            if(response.code==200){

            }
        }catch(e: Exception) {
            Log.e("TAG", "InformationActivity: ", e)
        }
    }
}