package cc.fastcv.uis.app.theoretical_basis.custom

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.appcompat.widget.AppCompatTextView

/**
 *
 * @author xcl
 * create at 2022/12/1 14:19
 * 按压回调器
 *
 */

class PressableTextView : AppCompatTextView {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    private var callback: OnPressedCallback? = null

    fun setCallback(callback: OnPressedCallback) {
        this.callback = callback
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val action = event?.action
        if (action == MotionEvent.ACTION_DOWN) {
            isPressed = true
            callback?.onPressed(true)
        } else if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_CANCEL) {
            isPressed = false
            callback?.onPressed(false)
        }
        return true
    }

    interface OnPressedCallback {
        fun onPressed(pressed:Boolean)
    }
}