package cc.fastcv.uis.app.theoretical_basis.custom

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.VelocityTracker
import android.view.View

class SimpleSpeedometer : View {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes)

    private var mVelocityTracker: VelocityTracker? = null

    private var callback: OnSpeedChangeCallback? = null

    fun setCallback(callback: OnSpeedChangeCallback) {
        this.callback = callback
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {

        if (mVelocityTracker == null) {
            mVelocityTracker = VelocityTracker.obtain()
        }

        when (event?.action) {
            MotionEvent.ACTION_DOWN ,MotionEvent.ACTION_MOVE -> {
                parent.requestDisallowInterceptTouchEvent(true)
                mVelocityTracker!!.addMovement(event);
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                //求伪瞬时速度

                //求伪瞬时速度
                mVelocityTracker!!.computeCurrentVelocity(1000)
                val velocityX = mVelocityTracker!!.xVelocity
                callback?.onSpeedChangeCallback(velocityX)
                mVelocityTracker = null
                parent.requestDisallowInterceptTouchEvent(false)
            }
            else -> {}
        }
        return true
    }

    interface OnSpeedChangeCallback {
        fun onSpeedChangeCallback(speed:Float)
    }

}