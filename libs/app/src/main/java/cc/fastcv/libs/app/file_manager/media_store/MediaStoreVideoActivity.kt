package cc.fastcv.libs.app.file_manager.media_store

import android.annotation.SuppressLint
import android.content.ContentUris
import android.content.Intent
import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import cc.fastcv.file_manager.VideoShowActivity
import cc.fastcv.lab_base.LeafActivity
import cc.fastcv.libs.app.R
import java.io.File
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList
import kotlin.math.max

class MediaStoreVideoActivity : LeafActivity() {

    companion object {
        private const val VIDEO_FIRST_FRAME_TIME_US = 1000L

        /**
         * 视频缩略图默认压缩尺寸
         */
        private const val THUMBNAIL_DEFAULT_COMPRESS_VALUE = 512f
    }

    private val captureVideo: ActivityResultContracts.CaptureVideo =
        ActivityResultContracts.CaptureVideo()

    private val openGalleryContact = ActivityResultContracts.StartActivityForResult()

    @SuppressLint("ObsoleteSdkInt")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_media_store_video)

        val captureVideoUri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            FileProvider.getUriForFile(
                this@MediaStoreVideoActivity,
                "cc.fastcv.libs.app.file_manager.fileprovider",
                File(externalCacheDir, "video.mp4")
            )
        } else {
            Uri.fromFile(File(externalCacheDir, "video.mp4")).apply {

            }
        }

        val videoContact = registerForActivityResult(
            captureVideo
        ) { result: Boolean ->
            if (result) {
                startActivity(Intent(this, VideoShowActivity::class.java).apply {
                    setData(captureVideoUri)
                })
            }
        }

        findViewById<Button>(R.id.bt1).setOnClickListener {
            videoContact.launch(
                captureVideoUri
            )
        }

        val openGalleryResult = registerForActivityResult(
            openGalleryContact
        ) { result: ActivityResult ->
            var uri: Uri? = null
            if (result.data != null) {
                uri = result.data!!.data
            }
            if (uri != null) {
                startActivity(Intent(this, VideoShowActivity::class.java).apply {
                    setData(uri)
                })
            }
        }

        findViewById<Button>(R.id.bt2).setOnClickListener {
            openGalleryResult.launch(
                Intent(
                    Intent.ACTION_PICK,
                    MediaStore.Video.Media.EXTERNAL_CONTENT_URI
                )
            )
        }

        findViewById<Button>(R.id.bt3).setOnClickListener {
            loadVideos()
        }
    }

    private fun loadVideos() {
        object : Thread() {
            override fun run() {
                try {
                    val videoList: ArrayList<Uri> = queryVideos()
                    runOnUiThread {
                        startActivity(
                            Intent(
                                this@MediaStoreVideoActivity,
                                VideoShowActivity::class.java
                            ).apply {
                                putParcelableArrayListExtra(Intent.EXTRA_STREAM, videoList)
                            })
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }.start()
    }

    @SuppressLint("Range")
    private fun queryVideos(): ArrayList<Uri> {
        val videos = ArrayList<Uri>()
        val projection: Array<String> = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            arrayOf(
                MediaStore.Video.Media._ID, MediaStore.Video.Media.SIZE,
                MediaStore.Video.Media.WIDTH,
                MediaStore.Video.Media.HEIGHT,
                MediaStore.Video.Media.DATA,
                MediaStore.Video.Media.DISPLAY_NAME,
                MediaStore.Video.Media.MIME_TYPE,
                MediaStore.Video.Media.DURATION,
                MediaStore.Video.Media.DATE_ADDED,
                MediaStore.Video.Media.DATE_MODIFIED,
                MediaStore.Video.Media.BITRATE
            )
        } else {
            arrayOf(
                MediaStore.Video.Media._ID, MediaStore.Video.Media.SIZE,
                MediaStore.Video.Media.WIDTH,
                MediaStore.Video.Media.HEIGHT,
                MediaStore.Video.Media.DATA,
                MediaStore.Video.Media.DISPLAY_NAME,
                MediaStore.Video.Media.MIME_TYPE,
                MediaStore.Video.Media.DURATION,
                MediaStore.Video.Media.DATE_ADDED,
                MediaStore.Video.Media.DATE_MODIFIED
            )
        }
        val selection = MediaStore.Video.Media.DATE_ADDED + " >= ?"
        val selectionArgs = arrayOf(dateToTimestamp(22, 10, 2008).toString() + "")
        val sortOrder = MediaStore.Video.Media.DATE_ADDED + " DESC"
        val cursor = contentResolver.query(
            MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
            projection,
            selection,
            selectionArgs,
            sortOrder
        )
            ?: return videos
        while (cursor.moveToNext()) {
            val mediaId = cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media._ID))
//            val size = cursor.getLong(cursor.getColumnIndex(MediaStore.Video.Media.SIZE))
//            val width = cursor.getFloat(cursor.getColumnIndex(MediaStore.Video.Media.WIDTH))
//            val height = cursor.getFloat(cursor.getColumnIndex(MediaStore.Video.Media.HEIGHT))
//            val localPath =
//                cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DATA))
            val localPathUri = ContentUris.withAppendedId(
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
                mediaId!!.toLong()
            )
//            val fileName =
//                cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.DISPLAY_NAME))
//            val mimeType =
//                cursor.getString(cursor.getColumnIndex(MediaStore.Video.Media.MIME_TYPE))
//            val firstFrame = getVideoThumbnail(localPathUri)
//            val duration =
//                cursor.getLong(cursor.getColumnIndex(MediaStore.Video.Media.DURATION))
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
//                val biteRate =
//                    cursor.getLong(cursor.getColumnIndex(MediaStore.Video.Media.BITRATE))
//            } else {
//                val biteRate =
//                    (8 * size * 1024 / (duration / 1000f)).toLong()
//            }
//            val addTime =
//                cursor.getLong(cursor.getColumnIndex(MediaStore.Video.Media.DATE_ADDED))
//            val lastModified =
//                cursor.getLong(cursor.getColumnIndex(MediaStore.Video.Media.DATE_MODIFIED))
            videos.add(localPathUri)
        }
        cursor.close()
        return videos
    }

    @SuppressLint("SimpleDateFormat")
    private fun dateToTimestamp(day: Int, month: Int, year: Int): Long {
        val simpleDateFormat = SimpleDateFormat("dd.MM.yyyy")
        val time: Long = try {
            simpleDateFormat.parse("$day.$month.$year")!!.time
        } catch (e: ParseException) {
            0L
        }
        return TimeUnit.MICROSECONDS.toSeconds(time)
    }

    /**
     * 获取视频缩略图：通过绝对路径抓取第一帧
     */
    private fun getVideoThumbnail(uri: Uri): Bitmap? {
        val bitmap: Bitmap?
        val retriever = MediaMetadataRetriever()
        try {
            retriever.setDataSource(this, uri)
            // OPTION_CLOSEST_SYNC：在给定的时间，检索最近一个同步与数据源相关联的的帧（关键帧）
            // OPTION_CLOSEST：表示获取离该时间戳最近帧（I帧或P帧）
            bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
                retriever.getScaledFrameAtTime(
                    VIDEO_FIRST_FRAME_TIME_US,
                    MediaMetadataRetriever.OPTION_CLOSEST_SYNC,
                    THUMBNAIL_DEFAULT_COMPRESS_VALUE.toInt(),
                    THUMBNAIL_DEFAULT_COMPRESS_VALUE.toInt()
                )
            } else {
                compressVideoThumbnail(retriever.getFrameAtTime(VIDEO_FIRST_FRAME_TIME_US))
            }
            return bitmap
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        } finally {
            try {
                retriever.release()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    /**
     * 压缩视频缩略图
     */
    private fun compressVideoThumbnail(bitmap: Bitmap?): Bitmap? {
        if (bitmap == null) return null
        val width = bitmap.getWidth()
        val height = bitmap.getHeight()
        val max = max(width.toDouble(), height.toDouble()).toInt()
        if (max > THUMBNAIL_DEFAULT_COMPRESS_VALUE) {
            val scale = THUMBNAIL_DEFAULT_COMPRESS_VALUE / max
            val w = (scale * width).toInt()
            val h = (scale * height).toInt()
            return compressVideoThumbnail(bitmap, w, h)
        }
        return bitmap
    }

    /**
     * 压缩视频缩略图：宽高压缩
     * 注：如果用户期望的长度和宽度和原图长度宽度相差太多的话，图片会很不清晰。
     *
     * @param bitmap 视频缩略图
     */
    private fun compressVideoThumbnail(bitmap: Bitmap, width: Int, height: Int): Bitmap {
        return Bitmap.createScaledBitmap(bitmap, width, height, true)
    }

}