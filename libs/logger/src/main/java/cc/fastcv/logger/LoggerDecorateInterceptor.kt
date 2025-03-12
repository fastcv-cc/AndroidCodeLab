package cc.fastcv.logger

import android.text.TextUtils

internal class LoggerDecorateInterceptor : AbsLogInterceptChain() {
    override fun intercept(priority: Int, tag: String, logMsg: String?, throwable: Throwable?) {
        val decorateMsg = "ThreadName: ${Thread.currentThread().name} ---> $logMsg"
        super.intercept(priority, if (TextUtils.isEmpty(FastLogger.tagPrefix)) {
            tag
        } else {
            "${FastLogger.tagPrefix}_$tag"
        }, decorateMsg, throwable)
    }
}