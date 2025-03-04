package cc.fastcv.logger

internal class LoggerDecorateInterceptor : AbsLogInterceptChain() {
    override fun intercept(priority: Int, tag: String, logMsg: String?, throwable: Throwable?) {
        val decorateMsg = "ThreadName: ${Thread.currentThread().name} ---> $logMsg"
        super.intercept(priority, "Umeox_$tag", decorateMsg, throwable)
    }
}