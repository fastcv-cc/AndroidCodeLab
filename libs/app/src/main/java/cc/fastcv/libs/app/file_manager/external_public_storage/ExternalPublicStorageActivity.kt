package cc.fastcv.libs.app.file_manager.external_public_storage

import android.Manifest
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import cc.fastcv.file_manager.FileManager
import cc.fastcv.lab_base.LeafActivity
import cc.fastcv.libs.app.R
import kotlinx.coroutines.launch

class ExternalPublicStorageActivity : LeafActivity() {

    private lateinit var intentLauncher: ActivityResultLauncher<Array<String>>

    private var needCheckExternalStorageManagerPermission = false

    private var resultCallback =
        ActivityResultCallback { result: Map<String, Boolean> ->
            if (!result.containsValue(false)) {
                Toast.makeText(this, "授权成功！！！", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "授权失败！！！", Toast.LENGTH_SHORT).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_external_public_storage)
        intentLauncher =
            registerForActivityResult(
                ActivityResultContracts.RequestMultiplePermissions(),
                resultCallback
            )
        findViewById<Button>(R.id.bt1).setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                requestExternalStorageManagerPermission()
            } else {
                requestExternalStoragePermission()
            }
        }

        findViewById<Button>(R.id.bt2).setOnClickListener {
            FileManager.openDicByPath(
                this,
                Environment.getExternalStorageDirectory().absolutePath
            )
        }

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                Log.d("xcl_debug", "onCreate: needCheckExternalStorageManagerPermission = $needCheckExternalStorageManagerPermission")
                if (needCheckExternalStorageManagerPermission) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                        if (Environment.isExternalStorageManager()) {
                            Toast.makeText(
                                this@ExternalPublicStorageActivity,
                                "授权成功！！！",
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            Toast.makeText(
                                this@ExternalPublicStorageActivity,
                                "授权失败！！！",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.R)
    private fun requestExternalStorageManagerPermission() {
        if (!Environment.isExternalStorageManager()) {
            val builder =
                AlertDialog.Builder(this)
                    .setMessage("本程序需要您同意允许访问所有文件权限")
                    .setPositiveButton(
                        "确定"
                    ) { _: DialogInterface?, _: Int ->
                        needCheckExternalStorageManagerPermission = true
                        startActivity(
                            Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION)
                        )
                    }.create()
            builder.show()
        } else {
            Toast.makeText(this, "您已经被授权此权限了！！！", Toast.LENGTH_SHORT).show()
        }
    }

    private fun requestExternalStoragePermission() {
        if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
            || checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
        ) {
            val builder =
                AlertDialog.Builder(this)
                    .setMessage("本程序需要您同意允许读写文件权限").setPositiveButton(
                        "确定"
                    ) { _: DialogInterface?, _: Int ->
                        intentLauncher.launch(
                            arrayOf(
                                Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE
                            )
                        )
                    }.create()
            builder.show()
        } else {
            Toast.makeText(this, "您已经被授权此权限了！！！", Toast.LENGTH_SHORT).show()
        }
    }


}