package cc.fastcv.libs.app

import android.app.Application
import cc.fastcv.logger.AbsLogInterceptChain
import cc.fastcv.logger.FastLogger

class LibsApp : Application() {

    private val TAG:String = "TAG"

    override fun onCreate() {
        super.onCreate()
        initLogger()
        FastLogger.i(TAG,"this is info level log")
        FastLogger.d(TAG,"this is debug level log")
        FastLogger.w(TAG,"this is warn level log")
        FastLogger.e(TAG,"this is error level log",NullPointerException("this is NullPointerException"))
    }

    private fun initLogger() {
        //设置上下文
        FastLogger.initLogger(this)
        //控制是否打印
        FastLogger.logEnable = true
        //控制是否保存到日志文件
        FastLogger.logSave2File = true
        //自定义打印
        FastLogger.setInterceptor(object : AbsLogInterceptChain() {
            override fun intercept(
                priority: Int,
                tag: String,
                logMsg: String?,
                throwable: Throwable?
            ) {
                //log text
            }
        })
    }

}