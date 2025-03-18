package cc.fastcv.file_manager

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.DocumentsContract
import android.view.View
import android.widget.TextView
import android.widget.VideoView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.documentfile.provider.DocumentFile
import java.text.DecimalFormat
import kotlin.math.log10
import kotlin.math.pow

class VideoShowActivity : AppCompatActivity() {

    private lateinit var tvPre: TextView
    private lateinit var tvNext: TextView
    private lateinit var video: VideoView

    private val videoUriList = mutableListOf<Uri>()

    private var index = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_show)
        initVideoUriList()
        if (videoUriList.isEmpty()) {
            showExceptionDialogAndExits("无视频数据！！！")
            return
        }

        tvPre = findViewById(R.id.tvPre)
        tvNext = findViewById(R.id.tvNext)
        video = findViewById(R.id.vv_show)
        val tv = findViewById<TextView>(R.id.tv)

        showOpButtonIfNeed()

        tvPre.setOnClickListener {
            if (videoUriList.size == 1) {
                return@setOnClickListener
            }
            index = (index - 1 + videoUriList.size) % videoUriList.size
            showFileInfo(videoUriList[index], tv)
        }

        tvNext.setOnClickListener {
            if (videoUriList.size == 1) {
                return@setOnClickListener
            }
            index = (index + 1 + videoUriList.size) % videoUriList.size
            showFileInfo(videoUriList[index], tv)
        }

        showFileInfo(videoUriList[index], tv)
    }

    private fun initVideoUriList() {
        val list = intent?.getParcelableArrayListExtra<Uri>(Intent.EXTRA_STREAM)
        if (!list.isNullOrEmpty()) {
            videoUriList.addAll(list)
            return
        }

        val clipData = intent?.clipData
        if (clipData != null && clipData.itemCount > 0) {
            for (i in 0 until clipData.itemCount) {
                val item = clipData.getItemAt(i)
                val uri = item.uri ?: continue
                videoUriList.add(uri)
            }
        } else {
            val uri = intent?.data
            if (uri == null) {
                showExceptionDialogAndExits("空数据！！！")
                return
            }
            videoUriList.add(uri)
        }
    }

    private fun showOpButtonIfNeed() {
        if (videoUriList.size > 1) {
            tvPre.isEnabled = true
            tvNext.isEnabled = true
            tvPre.visibility = View.VISIBLE
            tvNext.visibility = View.VISIBLE
        } else {
            tvPre.isEnabled = false
            tvNext.isEnabled = false
            tvPre.visibility = View.INVISIBLE
            tvNext.visibility = View.INVISIBLE
        }
    }

    @SuppressLint("SetTextI18n")
    private fun showFileInfo(uri: Uri, tv: TextView) {
        video.setVideoURI(uri)
        video.start()
        val documentFile = DocumentFile.fromSingleUri(this, uri)
        tv.text = "文件名称：${documentFile?.name}\n" +
                "文件类型：${documentFile?.type}\n" +
                "文件uri：${documentFile?.uri}\n" +
                "文件大小：${documentFile?.length()?.formatFileSize()}\n" +
                "是否是虚拟文件：${documentFile?.isVirtual}\n" +
                "是否是虚拟文件：${documentFile?.isVirtual}\n" +
                "是否是虚拟文件2：${isVirtualFile(uri)}\n" +
                "是否是文件：${documentFile?.isFile}\n" +
                "是否是目录：${documentFile?.isDirectory}\n" +
                "是否存在：${documentFile?.exists()}\n" +
                ""
    }

    private fun Long.formatFileSize(): String {
        if (this <= 0) {
            return "0 B"
        }

        val units = arrayOf("B", "KB", "MB", "GB", "TB")
        val digitGroups = (log10(toDouble()) / log10(1024.0)).toInt()
        return DecimalFormat("#,##0.#").format(
            this / 1024.0.pow(digitGroups.toDouble())
        ) + " " + units[digitGroups]
    }


    private fun isVirtualFile(uri: Uri): Boolean {
        if (!DocumentsContract.isDocumentUri(this, uri)) {
            return false
        }

        val cursor: Cursor? = contentResolver.query(
            uri,
            arrayOf(DocumentsContract.Document.COLUMN_FLAGS),
            null,
            null,
            null
        )

        val flags: Int = cursor?.use {
            if (cursor.moveToFirst()) {
                cursor.getInt(0)
            } else {
                0
            }
        } ?: 0

        return flags and DocumentsContract.Document.FLAG_VIRTUAL_DOCUMENT != 0
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

}