package cc.fastcv.libs.app.logger

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import cc.fastcv.libs.app.R
import cc.fastcv.logger.FastLogger

class LoggerActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_logger)
        initBt1()
    }

    //多线程打印日志
    private fun initBt1() {
        val threadLogTestFunction: (String, String) -> Unit = { tag, message ->
            Thread {
                run {
                    for (i in 0 until 10000) {
                        FastLogger.i(tag, "$message --- $i")
                        FastLogger.d(tag, "$message --- $i")
                        FastLogger.w(tag, "$message --- $i")
                        FastLogger.e(tag, "$message --- $i", NullPointerException("Exception"))
                    }
                }
            }.apply {
                name = tag
                start()
            }
        }
        findViewById<Button>(R.id.bt1).setOnClickListener {
            //线程1
            threadLogTestFunction("Thread1", "我是线程1，我打印Thread1")
            //线程2
            threadLogTestFunction("Thread2", "我是线程2，我打印Thread2")
            //线程3
            threadLogTestFunction("Thread3", "我是线程3，我打印Thread3")
            //线程4
            threadLogTestFunction("Thread4", "我是线程4，我打印Thread4")
            //线程5
            threadLogTestFunction("Thread5", "我是线程5，我打印Thread5")
            //线程6
            threadLogTestFunction("Thread6", "我是线程6，我打印Thread6")
            //线程7
            threadLogTestFunction("Thread7", "我是线程7，我打印Thread7")
            //线程8
            threadLogTestFunction("Thread8", "我是线程8，我打印Thread8")
            //线程9
            threadLogTestFunction("Thread9", "我是线程9，我打印Thread9")
            //线程10
            threadLogTestFunction("Thread10", "我是线程10，我打印Thread10")

        }
    }

}