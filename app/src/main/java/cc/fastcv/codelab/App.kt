package cc.fastcv.codelab

import android.app.Application
import cc.fastcv.app_trace.AppTracer
import cc.fastcv.logger.FastLogger

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        initLogger()
        AppTracer.attach(this)
    }

    private fun initLogger() {
        //设置上下文
        FastLogger.initLogger(this)
        //控制是否打印
        FastLogger.logEnable = true
        //控制是否保存到日志文件
        FastLogger.logSave2File = true
        //自定义打印
//        FastLogger.setInterceptor(object : AbsLogInterceptChain() {
//            override fun intercept(
//                priority: Int,
//                tag: String,
//                logMsg: String?,
//                throwable: Throwable?
//            ) {
//                //log text
//            }
//        })
    }
}