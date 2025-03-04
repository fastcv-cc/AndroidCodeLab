# Logger库说明文档

在日常开发中，日志收集是个非常重要的功能，很多时候，我们都是简单的使用Log.d直接打印我们想要的内容，这样开发当然没问题。包括说简单封装下，统一到一个接口方法打印，也是很常见的手法。我只是在这个基础上面，抽取个简单实现的库而已，没啥特别的。



# 用法

## 初始化

```
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
```



## 打印

```
        FastLogger.i(TAG,"this is info level log")
        FastLogger.d(TAG,"this is debug level log")
        FastLogger.w(TAG,"this is warn level log")
        FastLogger.e(TAG,"this is error level log",NullPointerException("this is NullPointerException"))
```



# 实现思路

整体实现思路用到了设计模式中的：责任链模式。（假装牛逼）

语言使用的是 Kotlin。

## 第一步：定义对外接口

首先我们定义我们对外的对象类 FastLogger，并且定义出我们对外的方法

```
object FastLogger {
		fun d(tag: String, message: String) {
    }

    fun e(tag: String, message: String, throwable: Throwable? = null) {
    }

    fun w(tag: String, message: String) {
    }

    fun i(tag: String, message: String) {
    }
}
```

这是四个我们常见的打印方法，我们的所有逻辑都是围绕这四个方法进行开发的。





## 第二步：实现打印方法

在实现这个方法之前，我们跟一下官方打印的调用流程：

android.util.Log

```
    public static int i(String tag, String msg) {
        return println(LOG_ID_MAIN, INFO, tag, msg);
    }
    public static int d(String tag, String msg) {
        return println(LOG_ID_MAIN, DEBUG, tag, msg);
    }   
    public static int w(String tag, String msg) {
        return println(LOG_ID_MAIN, WARN, tag, msg);
    }    
    public static int e(String tag, String msg, Throwable tr) {
        return println(LOG_ID_MAIN, ERROR, tag, msg + '\n' + getStackTraceString(tr));
    }
```

可以看到，最终都是调用的println方法

```
    public static int println(int bufID,int priority, String tag, String msg)
```

那么最终，其实就是需要以下几个参数：

- priority：日志级别
- tag：TAG
- msg：日志内容



所以，我基于此，定义我们的打印方法，并且通过一个变量控制日志是否可以打印。

```
object FastLogger {

    var logEnable = BuildConfig.DEBUG

    fun d(tag: String, message: String) {
        log(Log.DEBUG, message, tag)
    }

    fun e(tag: String, message: String, throwable: Throwable? = null) {
        log(Log.ERROR, message, tag, throwable = throwable)
    }

    fun w(tag: String, message: String) {
        log(Log.WARN, message, tag)
    }

    fun i(tag: String, message: String) {
        log(Log.INFO, message, tag)
    }

    @Synchronized
    private fun log(
        priority: Int,
        message: String,
        tag: String,
        throwable: Throwable? = null
    ) {
        if (logEnable) {
            Log.println(priority, message, tag + '\n' + Log.getStackTraceString(throwable));
        }
    }
}
```



在这一步，其实，我们一个打印库就封装好了。

只是在这个基础上面，我们实现的和官方提供的没啥区别，就是加了个开关控制而已。我们应该对其美化一下。



## 第三步：使用责任链装逼

首先定义一个抽象类

```
abstract class AbsLogInterceptChain {
    var next: AbsLogInterceptChain? = null

    open fun intercept(priority: Int, tag: String, logMsg: String?,throwable: Throwable? = null) {
        next?.intercept(priority, tag, logMsg,throwable)
    }
}
```



然后实现我们的默认打印类

```
internal class DefaultLoggerInterceptor : AbsLogInterceptChain() {

    override fun intercept(priority: Int, tag: String, logMsg: String?, throwable: Throwable?) {
        next?.intercept(priority, tag, logMsg, throwable)
    }
}
```



在这基础上面，对我们的日志内容进行美化下，于是添加装饰类

```
internal class LoggerDecorateInterceptor : AbsLogInterceptChain() {
    override fun intercept(priority: Int, tag: String, logMsg: String?, throwable: Throwable?) {
        val decorateMsg = "ThreadName: ${Thread.currentThread().name} ---> $logMsg"
        super.intercept(priority, "Umeox_$tag", decorateMsg, throwable)
    }
}
```



最后实现真正的打印类

```
internal class LoggerPrintInterceptor: AbsLogInterceptChain() {
    override fun intercept(priority: Int, tag: String, logMsg: String?,throwable: Throwable?) {
        when(priority) {
            Log.INFO -> {
                Log.i(tag, logMsg?:"-")
            }
            Log.WARN -> {
                Log.w(tag, logMsg?:"-")
            }
            Log.DEBUG -> {
                Log.d(tag, logMsg?:"-")
            }
            Log.ERROR -> {
                Log.e(tag, logMsg?:"-",throwable)
            }
            Log.VERBOSE -> {
                Log.w(tag, logMsg?:"-",throwable)
            }
        }
        super.intercept(priority, tag, logMsg, throwable)
    }
}
```



在开发过程中，还有保存日志到本地的需求，如果又添加了保存到本地的链接类

```
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
            val needWriteMessage: String = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA).format(date)
                .toString() + "    " + tag + "    " + logMsg + "\n"
            try {
                file!!.appendText(needWriteMessage)
            } catch (e: Exception) {
                return@post
            }
        }
    }
}
```



通过责任链的关系，我们FastLogger最终的实现就是

```
object FastLogger {

    private var initialized = false

    private var intercepts:AbsLogInterceptChain = DefaultLoggerInterceptor()

    var logEnable = BuildConfig.DEBUG

    var logSave2File = false

    fun initLogger(context: Context) {
        if (initialized) {
            initialized = true
            return
        }

        val fileInterceptor = Logger2FileInterceptor(context)

        val printInterceptor = LoggerPrintInterceptor()
        printInterceptor.next = fileInterceptor

        val decorateInterceptor = LoggerDecorateInterceptor()
        decorateInterceptor.next = printInterceptor

        intercepts.next = decorateInterceptor
    }

    fun d(tag: String, message: String) {
        log(Log.DEBUG, message, tag)
    }

    fun e(tag: String, message: String, throwable: Throwable? = null) {
        log(Log.ERROR, message, tag, throwable = throwable)
    }

    fun w(tag: String, message: String) {
        log(Log.WARN, message, tag)
    }

    fun i(tag: String, message: String) {
        log(Log.INFO, message, tag)
    }

    fun setInterceptor(interceptor: AbsLogInterceptChain) {
        this.intercepts = interceptor
    }

    @Synchronized
    private fun log(
        priority: Int,
        message: String,
        tag: String,
        throwable: Throwable? = null
    ) {
        if (logEnable) {
            intercepts.intercept(priority, tag, message, throwable)
        }
    }
}
```



其实，在这个基础上面，我们可以抽取出文本保存地址呀，定期清除的时长呀等等参数，这个你们可以下去继续封装。