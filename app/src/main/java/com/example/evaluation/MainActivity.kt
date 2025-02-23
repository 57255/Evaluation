package com.example.evaluation


import android.Manifest
import android.app.Activity
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.evaluation.databinding.ActivityMainBinding
import com.example.evaluation.utils.AutoUpdater
import com.google.android.material.bottomnavigation.BottomNavigationView


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        navView.setupWithNavController(navController)
        setAndroidNativeLightStatusBar(this, true)
        //检查更新
        //检查更新
        try {
            //6.0才用动态权限
            if (Build.VERSION.SDK_INT >= 23) {
                val permissions = arrayOf<String>(
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.ACCESS_WIFI_STATE,
                    Manifest.permission.INTERNET
                )
                val permissionList: MutableList<String> = ArrayList()
                for (i in permissions.indices) {
                    if (ActivityCompat.checkSelfPermission(
                            this,
                            permissions[i]
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        permissionList.add(permissions[i])
                    }
                }
                if (permissionList.size <= 0) {
                    //说明权限都已经通过，可以做你想做的事情去
                    //自动更新
                    val manager = AutoUpdater(this@MainActivity)
                    manager.CheckUpdate()
                } else {
                    //存在未允许的权限
                    ActivityCompat.requestPermissions(this, permissions, 100)
                }
            }
        } catch (ex: Exception) {
            Toast.makeText(this@MainActivity, "自动更新异常：" + ex.message, Toast.LENGTH_SHORT).show()
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

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        var hasPermission = false
        if (requestCode == 100) {
            for (result in grantResults) {
                if (result == -1) {
                    hasPermission = true
                }
            }
            if (!hasPermission) {
                // 跳转到系统设置权限页面，或者直接关闭页面，不让他继续访问
                permissionDialog()
            } else {
                // 全部权限通过，可以进行下一步操作
                val manager = AutoUpdater(this@MainActivity)
                manager.CheckUpdate()
            }
        }
    }

    private var alertDialog: AlertDialog? = null

    // 打开手动设置应用权限
    private fun permissionDialog() {
        if (alertDialog == null) {
            alertDialog = AlertDialog.Builder(this)
                .setTitle("提示信息")
                .setMessage("当前应用缺少必要权限，该功能暂时无法使用。如若需要，请单击【确定】按钮前往设置中心进行权限授权。")
                .setPositiveButton("设置") { dialog, which ->
                    cancelPermissionDialog()
                    val packageURI = Uri.parse("package:$packageName")
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, packageURI)
                    startActivity(intent)
                }
                .setNegativeButton("取消") { dialog, which ->
                    cancelPermissionDialog()
                }
                .create()
        }
        alertDialog?.show()
    }

    private fun cancelPermissionDialog() {
        alertDialog?.cancel()
    }



}