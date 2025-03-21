package cc.fastcv.uis.app.marquee

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import cc.fastcv.uis.app.R

class MarqueeTextViewActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_marquee)
        findViewById<TextView>(R.id.amigoAddress).apply {
            post {
                isSelected = true
            }
        }
//        findViewById<MarqueeTextView>(R.id.amigoAddress).setText("我是一段比较长的文字哈哈哈哈哈哈哈哈啊哈哈",true)

        findViewById<Button>(R.id.bt).setOnClickListener {
            findViewById<MarqueeTextView>(R.id.amigoAddress).apply {
                setText("我是修改之后的文字我是修改之后的文字我是修改之后的文字我是修改之后的文字我是修改之后的文字",true)
            }
        }


    }

}