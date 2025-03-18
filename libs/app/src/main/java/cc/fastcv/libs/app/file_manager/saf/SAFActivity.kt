package cc.fastcv.libs.app.file_manager.saf

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Button
import androidx.core.net.toUri
import androidx.lifecycle.lifecycleScope
import cc.fastcv.file_manager.FileListManagerActivity
import cc.fastcv.file_manager.FileManager
import cc.fastcv.file_manager.MimeType
import cc.fastcv.lab_base.LeafActivity
import cc.fastcv.libs.app.R
import cc.fastcv.local_data_store.LocalDataStore
import kotlinx.coroutines.launch

class SAFActivity : LeafActivity() {

    companion object {
        private const val TAG = "SAFActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_saf)
        findViewById<Button>(R.id.bt1).setOnClickListener {
            startActivityForResult(Intent(Intent.ACTION_PICK).apply {
                //设置访问类型
                type = MimeType._images.value
            }, 1001)
        }

        findViewById<Button>(R.id.bt2).setOnClickListener {
            startActivityForResult(Intent(Intent.ACTION_GET_CONTENT).apply {
                // 只选择图片: intent.type = "image/*"
                // 只选择视频: intent.type = "video/*"
                type = MimeType._images.value
                // 支持多选（长按多选）
                putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
                //仅仅选择本地的
                putExtra(Intent.EXTRA_LOCAL_ONLY, true)
                // 用于表示 Intent 仅希望查询能使用 ContentResolver.openFileDescriptor(Uri, String) 打开的 Uri
                addCategory(Intent.CATEGORY_OPENABLE)
            }, 1002)
        }

        findViewById<Button>(R.id.bt3).setOnClickListener {
            startActivityForResult(Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                // 只选择图片: intent.type = "image/*"
                // 只选择视频: intent.type = "video/*"
                type = MimeType._images.value
                // 支持多选（长按多选）
                putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
                //仅仅选择本地的
                putExtra(Intent.EXTRA_LOCAL_ONLY, true)
                // 用于表示 Intent 仅希望查询能使用 ContentResolver.openFileDescriptor(Uri, String) 打开的 Uri
                addCategory(Intent.CATEGORY_OPENABLE)
            }, 1003)
        }

        findViewById<Button>(R.id.bt3Restart).setOnClickListener {
            lifecycleScope.launch {
                val oldUriStr = LocalDataStore.getString(
                    this@SAFActivity,
                    "file_manager",
                    "action_open_document_uri",
                    ""
                )
                if (!TextUtils.isEmpty(oldUriStr)) {
                    FileListManagerActivity.intoActivity(
                        this@SAFActivity,
                        arrayListOf(oldUriStr.toUri())
                    )
                }
            }
        }

        findViewById<Button>(R.id.bt4).setOnClickListener {
            startActivityForResult(Intent(Intent.ACTION_OPEN_DOCUMENT_TREE), 1004)
        }

        findViewById<Button>(R.id.bt5).setOnClickListener {
            for (persistedUriPermission in contentResolver.persistedUriPermissions) {
                Log.d(TAG, "persistedUriPermission : $persistedUriPermission")
            }
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1001 && resultCode == RESULT_OK) {
            val uri = data?.data ?: return
            Log.d(TAG, "onActivityResult: ACTION_PICK uri = $uri")
            FileListManagerActivity.intoActivity(this@SAFActivity, arrayListOf(uri))
        } else if (requestCode == 1002 && resultCode == RESULT_OK) {
            val clipData = data?.clipData
            val arrayList = ArrayList<Uri>()
            if (clipData != null && clipData.itemCount > 0) {
                // 多选的情况
                for (i in 0 until clipData.itemCount) {
                    val item = clipData.getItemAt(i)
                    val uri = item.uri ?: continue
                    Log.d(TAG, "onActivityResult: ACTION_GET_CONTENT uri = $uri")
                    arrayList.add(uri)
                }
            } else {
                // 单选的情况
                val uri = data?.data ?: return
                Log.d(TAG, "onActivityResult: ACTION_GET_CONTENT uri = $uri")
                arrayList.add(uri)
            }
            FileListManagerActivity.intoActivity(this@SAFActivity, arrayList)
        } else if (requestCode == 1003 && resultCode == RESULT_OK) {
            val clipData = data?.clipData
            val arrayList = ArrayList<Uri>()
            if (clipData != null && clipData.itemCount > 0) {
                // 多选的情况
                for (i in 0 until clipData.itemCount) {
                    val item = clipData.getItemAt(i)
                    val uri = item.uri ?: continue
                    Log.d(TAG, "onActivityResult: ACTION_OPEN_DOCUMENT uri = $uri")
                    arrayList.add(uri)
                }
            } else {
                // 单选的情况
                val uri = data?.data ?: return
                Log.d(TAG, "onActivityResult: ACTION_OPEN_DOCUMENT uri = $uri")
                lifecycleScope.launch {
                    LocalDataStore.saveString(
                        this@SAFActivity,
                        "file_manager",
                        "action_open_document_uri",
                        uri.toString()
                    )
                    try {
                        application.contentResolver.takePersistableUriPermission(
                            uri,
                            Intent.FLAG_GRANT_READ_URI_PERMISSION
                        )
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
                arrayList.add(uri)
            }
            FileListManagerActivity.intoActivity(this@SAFActivity, arrayList)
        } else if (requestCode == 1004 && resultCode == RESULT_OK) {
            val uri = data?.data ?: return
            Log.d(TAG, "onActivityResult: ACTION_OPEN_DOCUMENT_TREE uri = $uri")
            FileManager.openDicByUri(this@SAFActivity, uri)
        }
    }

}