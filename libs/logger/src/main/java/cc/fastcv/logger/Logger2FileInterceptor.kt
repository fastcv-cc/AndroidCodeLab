package cc.fastcv.logger

import android.content.Context
import android.os.Handler
import android.os.HandlerThread
import android.util.Log
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

internal class Logger2FileInterceptor(private val context: Context): AbsLogInterceptChain() {

    private var file: File? = null
    private val handlerThread = HandlerThread("logger_to_file_thread")

    init {
        handlerThread.start()
    }

    private fun init() {
        Handler(handlerThread.looper).post {
            val format = SimpleDateFormat("yyyy-MM-dd", Locale.CHINA).format(Date())
            context.externalCacheDir?.let {
                file = File(it,"$format.txt")
                if (!file!!.exists()) {
                    try {
                        //在指定的文件夹中创建文件
                        file!!.createNewFile()
                    } catch (e: Exception) {
                        return@post
                    }
                }

                //删除无用文件 只存放三天的数据
                it.listFiles()?.let { files ->
                    for (file in files) {
                        try {
                            val old = SimpleDateFormat(
                                "yyyy-MM-dd",
                                Locale.CHINA
                            ).parse(file.name.replace(".txt", "")) ?: return@let

                            val timeInMillis = Calendar.getInstance().apply {
                                time = old
                            }.timeInMillis

                            val dDay = (System.currentTimeMillis() - timeInMillis) / 1000 / 60 /60 /24
                            if (dDay > 3) {
                                file.delete()
                            }
                        } catch (e:Exception) {
                        }
                    }
                }
            }
        }
    }

    override fun intercept(priority: Int, tag: String, logMsg: String?,throwable: Throwable?) {
        if (!FastLogger.logSave2File && priority != Log.VERBOSE) {
            return
        }
        Handler(handlerThread.looper).post {
            if (file == null) {
                init()
                intercept(priority, tag, logMsg, throwable)
                return@post
            }

            val date = Date()
            val needWriteMessage: String = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.CHINA).format(date)
                .toString() + "    " + tag + "    " + logMsg + "\n"
            try {
                file!!.appendText(needWriteMessage)
            } catch (e: Exception) {
                e.printStackTrace()
                return@post
            }
        }
    }
}