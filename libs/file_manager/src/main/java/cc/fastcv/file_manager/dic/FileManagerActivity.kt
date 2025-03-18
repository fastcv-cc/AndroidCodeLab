package cc.fastcv.file_manager.dic

import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import cc.fastcv.file_manager.R
import cc.fastcv.recyclerview_ext.OnItemClickListener
import cc.fastcv.recyclerview_ext.OnItemLongClickListener
import java.io.File
import java.util.Objects


internal class FileManagerActivity : AppCompatActivity(), OnItemClickListener<File>,
    OnItemLongClickListener<File> {

    companion object {
        private const val TAG = "FileManagerActivity"

        private const val FILE_PATH = "filePath"

        fun intoActivity(context: Context, filePath: String) {
            val intent = Intent(context, FileManagerActivity::class.java)
            intent.putExtra(FILE_PATH, filePath)
            context.startActivity(intent)
        }
    }

    private lateinit var tvPath: TextView
    private lateinit var root: File
    private lateinit var currentParentFile: File
    private lateinit var adapter: FileAdapter
    private lateinit var rvFile: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_file_manager)

        rvFile = findViewById(R.id.rv_file)
        tvPath = findViewById(R.id.tv_path)

        val filePath = intent.getStringExtra(FILE_PATH)

        if (TextUtils.isEmpty(filePath)) {
            showExceptionDialogAndExits("传入路径为空，将退出此界面！！")
            return
        }

        //默认文件夹一定存在
        root = File(filePath!!)
        if (!root.exists() || !root.isDirectory()) {
            showExceptionDialogAndExits("此路径不存在或者不为文件夹，将退出此界面！！")
            return
        }

        val files = root.listFiles()
        if (files == null) {
            showExceptionDialogAndExits("路径异常，将退出此界面！！")
            return
        }

        Log.d(TAG, "onCreate: root = ${root.absolutePath}")

        currentParentFile = root
        tvPath.text = currentParentFile.absolutePath
        adapter = FileAdapter()
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

    override fun onItemClick(view: View?, position: Int, t: File) {
        if (t.isDirectory()) {
            goDir(t)
        }
    }

    override fun onItemLongClick(view: View?, position: Int, t: File) {
        deleteFileOrFolder(t)
    }

    private fun deleteFileOrFolder(file: File) {
        val msg: String = if (file.isDirectory()) {
            "确认删除此文件夹？"
        } else {
            "确认删除此文件？"
        }
        AlertDialog.Builder(this)
            .setTitle("警告")
            .setMessage(msg)
            .setPositiveButton("确定") { _: DialogInterface?, _: Int ->
                val fileType: String = if (file.isDirectory()) {
                    "文件夹"
                } else {
                    "文件"
                }
                if (file.delete()) {
                    Toast.makeText(
                        this@FileManagerActivity,
                        fileType + "删除成功",
                        Toast.LENGTH_SHORT
                    ).show()
                    goDir(currentParentFile)
                } else {
                    Toast.makeText(
                        this@FileManagerActivity,
                        fileType + "删除失败",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            .create().show()
    }

    private fun goDir(file: File) {
        val listFiles = file.listFiles()
        if (listFiles == null) {
            Toast.makeText(this, "访问的目录不存在或无法访问", Toast.LENGTH_SHORT).show()
            return
        }
        currentParentFile = file
        tvPath.text = currentParentFile.absolutePath
        adapter.setData(listFiles.asList())
    }

    @SuppressLint("MissingSuperCall")
    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        if (currentParentFile.absolutePath.equals(root.absolutePath)) {
            onBackPressedDispatcher.onBackPressed()
        } else {
            goDir(Objects.requireNonNull(currentParentFile.getParentFile()))
        }
    }

}