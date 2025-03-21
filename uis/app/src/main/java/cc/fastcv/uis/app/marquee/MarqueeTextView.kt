package cc.fastcv.uis.app.marquee

import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import android.util.Log
import androidx.appcompat.widget.AppCompatTextView

class MarqueeTextView : AppCompatTextView {

    private var LAYOUT_DIRECTION = LAYOUT_DIRECTION_RTL
    private var TEXT_DIRECTION = TEXT_DIRECTION_RTL

    /**
     * 滚动次数
     */
    private var marqueeNum = -1 //-1为永久循环，大于0是循环次数。

    constructor(context: Context) : super(context) {
        setAttr()
    }
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        setAttr()
    }
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        setAttr()
    }

    /**
     * 设置相关属性
     */
    private fun setAttr() {
        this.ellipsize = TextUtils.TruncateAt.MARQUEE //设置跑马等效果
        this.marqueeRepeatLimit = marqueeNum //设置跑马灯重复次数
        this.isSingleLine = true //设置单行
    }

    fun setMarqueeNum(marqueeNum: Int) {
        this.marqueeNum = marqueeNum
    }

    /**
     * 始终获取焦点
     * 跑马灯在TextView处于焦点状态的时候才会滚动
     */
    override fun isFocused(): Boolean {
        return true
    }

    override fun getLayoutDirection(): Int {
        return LAYOUT_DIRECTION
    }

    override fun getTextDirection(): Int {
        return TEXT_DIRECTION
    }

    override fun requestLayout() {
        super.requestLayout()
        Log.d("xcl_debug", "requestLayout: ")
    }

    fun setText(content: String?, moveToLeft: Boolean) {
        LAYOUT_DIRECTION = if (moveToLeft) LAYOUT_DIRECTION_LTR else LAYOUT_DIRECTION_RTL
        TEXT_DIRECTION = if (moveToLeft) TEXT_DIRECTION_LTR else TEXT_DIRECTION_RTL
        text = content
    }
}