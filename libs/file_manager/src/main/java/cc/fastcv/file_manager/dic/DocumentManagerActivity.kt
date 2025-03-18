package cc.fastcv.file_manager.dic

import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.documentfile.provider.DocumentFile
import androidx.recyclerview.widget.RecyclerView
import cc.fastcv.file_manager.R
import cc.fastcv.recyclerview_ext.OnItemClickListener
import cc.fastcv.recyclerview_ext.OnItemLongClickListener

class DocumentManagerActivity : AppCompatActivity(), OnItemClickListener<DocumentFile>,
    OnItemLongClickListener<DocumentFile> {

    companion object {
        private const val TAG = "DocumentManagerActivity"

        private const val URI_PATH = "uriPath"

        fun intoActivity(context: Context, uri: Uri) {
            val intent = Intent(context, DocumentManagerActivity::class.java)
            intent.putExtra(URI_PATH, uri.toString())
            context.startActivity(intent)
        }
    }

    private lateinit var tvPath: TextView
    private var root: DocumentFile? = null
    private lateinit var currentParentFile: DocumentFile
    private lateinit var adapter: DocumentFileAdapter
    private lateinit var rvFile: RecyclerView

    private val filePathList = mutableListOf(".")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_file_manager)

        rvFile = findViewById(R.id.rv_file)
        tvPath = findViewById(R.id.tv_path)

        val uriPath = intent.getStringExtra(URI_PATH)

        if (TextUtils.isEmpty(uriPath)) {
            showExceptionDialogAndExits("传入路径为空，将退出此界面！！")
            return
        }

        val uri = Uri.parse(uriPath)
        root = DocumentFile.fromTreeUri(this, uri)
        if (root == null) {
            showExceptionDialogAndExits("传入路径不是个目录DocumentFile！！")
            return
        }

        if (!root!!.exists() || !root!!.isDirectory) {
            showExceptionDialogAndExits("此路径不存在或者不为文件夹，将退出此界面！！")
            return
        }

        val files = root!!.listFiles()
        Log.d(TAG, "onCreate: uriPath = $uriPath   root.type = ${root!!.type}")

        currentParentFile = root!!
        tvPath.text = filePathList.joinToString("/")
        adapter = DocumentFileAdapter()
        rvFile.setAdapter(adapter)
        adapter.setOnItemClickListener(this)
        adapter.setOnItemLongClickListener(this)
        adapter.setData(files.asList())


    }

    private fun showExceptionDialogAndExits(msg: String) {
        AlertDialog.Builder(this)
            .setTitle("警告")
            .setMessage(msg)
            .setCancelable(false)
            .setPositiveButton(
                "确定"
            ) { _: DialogInterface?, _: Int -> finish() }
            .create().show()
    }

    override fun onItemClick(view: View?, position: Int, t: DocumentFile) {
        if (t.isDirectory) {
            filePathList.add(t.name?:"-")
            goDir(t)
        }
    }

    override fun onItemLongClick(view: View?, position: Int, t: DocumentFile) {
        deleteFileOrFolder(t)
    }

    private fun deleteFileOrFolder(file: DocumentFile) {
        val msg: String = if (file.isDirectory) {
            "确认删除此文件夹？"
        } else {
            "确认删除此文件？"
        }
        AlertDialog.Builder(this)
            .setTitle("警告")
            .setMessage(msg)
            .setPositiveButton("确定") { _: DialogInterface?, _: Int ->
                val fileType: String = if (file.isDirectory) {
                    "文件夹"
                } else {
                    "文件"
                }
                if (file.delete()) {
                    Toast.makeText(
                        this@DocumentManagerActivity,
                        fileType + "删除成功",
                        Toast.LENGTH_SHORT
                    ).show()
                    goDir(currentParentFile)
                } else {
                    Toast.makeText(
                        this@DocumentManagerActivity,
                        fileType + "删除失败",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            .create().show()
    }

    private fun goDir(file: DocumentFile) {
        val listFiles = file.listFiles()
        currentParentFile = file
        tvPath.text = filePathList.joinToString("/")
        adapter.setData(listFiles.asList())
    }

    @SuppressLint("MissingSuperCall")
    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        if (currentParentFile.uri.toString() == root!!.uri.toString()) {
            onBackPressedDispatcher.onBackPressed()
        } else {
            filePathList.removeAt(filePathList.size - 1)
            goDir(currentParentFile.parentFile!!)
        }
    }

}