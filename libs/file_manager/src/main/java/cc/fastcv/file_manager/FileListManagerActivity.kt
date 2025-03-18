package cc.fastcv.file_manager

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cc.fastcv.recyclerview_ext.OnItemClickListener
import cc.fastcv.recyclerview_ext.OnItemLongClickListener


class FileListManagerActivity : AppCompatActivity(), OnItemClickListener<FastFile>,
    OnItemLongClickListener<FastFile> {

    companion object {
        private const val TAG = "FileListManagerActivity"

        fun intoActivity(context: Context, uris: ArrayList<Uri>) {
            val intent = Intent(context, FileListManagerActivity::class.java).apply {
                putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris)
            }
            context.startActivity(intent)
        }
    }

    private lateinit var tvPath: TextView
    private lateinit var adapter: FastFileAdapter
    private lateinit var rvFile: RecyclerView

    private val fileList = mutableListOf<FastFile>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_file_manager)

        rvFile = findViewById(R.id.rv_file)
        tvPath = findViewById(R.id.tv_path)
        tvPath.text = "---文件列表---"

        initFileList()
        if (fileList.isEmpty()) {
            showExceptionDialogAndExits("文件列表为空！！！")
            return
        }

        adapter = FastFileAdapter()
        val gridVerticalLayoutManager =
            GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false)
        rvFile.layoutManager = gridVerticalLayoutManager
        rvFile.setAdapter(adapter)
        adapter.setOnItemClickListener(this)
        adapter.setOnItemLongClickListener(this)
        adapter.setData(fileList)
    }

    private fun initFileList() {
        val list = intent?.getParcelableArrayListExtra<Uri>(Intent.EXTRA_STREAM)
        if (!list.isNullOrEmpty()) {
            list.forEach {
                fileList.add(FastFile(it))
            }
        }
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

    override fun onItemClick(view: View?, position: Int, t: FastFile) {

    }

    override fun onItemLongClick(view: View?, position: Int, t: FastFile) {
    }


}