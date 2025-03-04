package cc.fastcv.logger

abstract class AbsLogInterceptChain {
    var next: AbsLogInterceptChain? = null

    open fun intercept(priority: Int, tag: String, logMsg: String?,throwable: Throwable? = null) {
        next?.intercept(priority, tag, logMsg,throwable)
    }
}