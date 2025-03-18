package cc.fastcv.file_manager

import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.os.Build
import android.text.TextUtils
import android.webkit.MimeTypeMap
import androidx.documentfile.provider.DocumentFile
import java.io.File
import kotlin.math.max


class FastFile(val uri: Uri) {

    companion object {
        private const val SCHEME_HTTP = "http"
        private const val SCHEME_HTTPS = "https"

        private const val VIDEO_FIRST_FRAME_TIME_US = 1000L

        /**
         * 视频缩略图默认压缩尺寸
         */
        private const val THUMBNAIL_DEFAULT_COMPRESS_VALUE = 512f
    }

    private fun Uri.getMimeType(context: Context): String {
        return MimeTypeMap.getSingleton()
            .getMimeTypeFromExtension(getExtensionName(context))
            .orEmpty()
    }

    private fun Uri.getExtensionName(context: Context): String {
        val typeMap = MimeTypeMap.getSingleton()
        when (scheme) {
            ContentResolver.SCHEME_FILE -> {
                val url = Uri.fromFile(File(path ?: "")).toString()
                return MimeTypeMap.getFileExtensionFromUrl(url)
            }

            ContentResolver.SCHEME_CONTENT -> {
                val type = context.contentResolver.getType(this)
                return typeMap.getExtensionFromMimeType(type).orEmpty()
            }

            ContentResolver.SCHEME_ANDROID_RESOURCE -> {
                val retriever = MediaMetadataRetriever()
                retriever.setDataSource(context, this)
                val type = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_MIMETYPE)
                return typeMap.getExtensionFromMimeType(type).orEmpty()
            }

            SCHEME_HTTP,
            SCHEME_HTTPS -> return ""

            else -> return ""
        }
    }

    fun getMineType(context: Context): MimeType {
        var type = uri.getMimeType(context)
        if (TextUtils.isEmpty(type)) {
            val documentFile = DocumentFile.fromSingleUri(context, uri)
            type = documentFile?.type ?: ""
        }
        return when (type) {
            MimeType._png.value -> MimeType._png
            MimeType._images.value -> MimeType._images
            MimeType._jpeg.value -> MimeType._jpeg
            MimeType._webp.value -> MimeType._webp
            MimeType._gif.value -> MimeType._gif
            MimeType._bmp.value -> MimeType._bmp
            MimeType._3gp.value -> MimeType._3gp
            MimeType._apk.value -> MimeType._apk
            MimeType._asf.value -> MimeType._asf
            MimeType._avi.value -> MimeType._avi
            MimeType._bin.value -> MimeType._bin
            MimeType._text.value -> MimeType._text
            MimeType._class.value -> MimeType._class
            MimeType._doc.value -> MimeType._doc
            MimeType._docx.value -> MimeType._docx
            MimeType._xls.value -> MimeType._xls
            MimeType._xlsx.value -> MimeType._xlsx
            MimeType._exe.value -> MimeType._exe
            MimeType._gtar.value -> MimeType._gtar
            MimeType._gz.value -> MimeType._gz
            MimeType._htm.value -> MimeType._htm
            MimeType._html.value -> MimeType._html
            MimeType._jar.value -> MimeType._jar
            MimeType._js.value -> MimeType._js
            MimeType._m3u.value -> MimeType._m3u
            MimeType._m4a.value -> MimeType._m4a
            MimeType._m4b.value -> MimeType._m4b
            MimeType._m4p.value -> MimeType._m4p
            MimeType._m4u.value -> MimeType._m4u
            MimeType._m4v.value -> MimeType._m4v
            MimeType._mov.value -> MimeType._mov
            MimeType._mp2.value -> MimeType._mp2
            MimeType._mp3.value -> MimeType._mp3
            MimeType._mp4.value -> MimeType._mp4
            MimeType._mpc.value -> MimeType._mpc
            MimeType._mpe.value -> MimeType._mpe
            MimeType._mpeg.value -> MimeType._mpeg
            MimeType._mpg.value -> MimeType._mpg
            MimeType._mpg4.value -> MimeType._mpg4
            MimeType._mpga.value -> MimeType._mpga
            MimeType._msg.value -> MimeType._msg
            MimeType._ogg.value -> MimeType._ogg
            MimeType._pdf.value -> MimeType._pdf
            MimeType._pps.value -> MimeType._pps
            MimeType._ppt.value -> MimeType._ppt
            MimeType._pptx.value -> MimeType._pptx
            MimeType._rmvb.value -> MimeType._rmvb
            MimeType._rtf.value -> MimeType._rtf
            MimeType._tar.value -> MimeType._tar
            MimeType._tgz.value -> MimeType._tgz
            MimeType._wav.value -> MimeType._wav
            MimeType._wma.value -> MimeType._wma
            MimeType._wmv.value -> MimeType._wmv
            MimeType._wps.value -> MimeType._wps
            MimeType._z.value -> MimeType._z
            MimeType._zip.value -> MimeType._zip
            else -> MimeType._all
        }
    }

    /**
     * 获取视频缩略图：通过绝对路径抓取第一帧
     */
    fun getVideoThumbnail(context: Context): Bitmap? {
        val bitmap: Bitmap?
        val retriever = MediaMetadataRetriever()
        try {
            retriever.setDataSource(context, uri)
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