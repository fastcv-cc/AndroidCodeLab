package cc.fastcv.uis.app.theoretical_basis.custom

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

/**
 *
 * @author xcl
 * create at 2022/12/1 10:12
 * 用于测试点击位置的控件
 * 回调点击位置处于View中的x,y值、处于屏幕中的x,y值
 *
 */

class ClickShowView : View {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    private var callback: OnClickPointerCallback? = null

    private var clickPoint: PointF? = null

    private val mPaint = Paint().apply {
        style = Paint.Style.STROKE
        color = Color.RED
        strokeWidth = 5.0f
        isAntiAlias = true
    }

    fun setOnClickPointerCallback(callback: OnClickPointerCallback) {
        this.callback = callback
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val ev = event?.action
        if (ev == MotionEvent.ACTION_DOWN) {

            callback?.let {
                if (clickPoint == null) {
                    clickPoint = PointF(event.x, event.y)
                } else {
                    clickPoint!!.x = event.x
                    clickPoint!!.y = event.y
                }
                it.onView(event.x, event.y)
                it.onScreen(event.rawX, event.rawY)
                invalidate()
            }
        }
        return super.onTouchEvent(event)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        clickPoint?.let {
            canvas.drawCircle(it.x, it.y, 3.0f, mPaint)
            canvas.drawCircle(it.x, it.y, 10.0f, mPaint)
            canvas.drawCircle(it.x, it.y, 17.0f, mPaint)
        }
    }

    interface OnClickPointerCallback {
        fun onView(x: Float, y: Float)
        fun onScreen(x: Float, y: Float)
    }
}