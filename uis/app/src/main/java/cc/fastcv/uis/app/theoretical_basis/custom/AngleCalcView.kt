package cc.fastcv.uis.app.theoretical_basis.custom

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import kotlin.math.*

class AngleCalcView : View {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    private var mHeight = 0
    private var mWeight = 0

    private var mDeg = -1.0f

    private var callback: OnAngleChangeCallback? = null

    fun setCallback(callback: OnAngleChangeCallback) {
        this.callback = callback
    }

    private val mPaint = Paint().apply {
        style = Paint.Style.STROKE
        color = Color.RED
        strokeWidth = 5.0f
        isAntiAlias = true
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        var widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val widthMode = MeasureSpec.getMode(heightMeasureSpec)

        if (widthMode == MeasureSpec.AT_MOST) {
            widthSize = resources.displayMetrics.widthPixels
        }

        var heightSize = MeasureSpec.getSize(heightMeasureSpec)
        heightSize = max(widthSize, heightSize)

        Log.d("xcl_debug", "onMeasure: widthSize = $widthSize  heightSize = $heightSize")
        val min = min(widthSize, heightSize)

        setMeasuredDimension(
            MeasureSpec.makeMeasureSpec(min, MeasureSpec.EXACTLY),
            MeasureSpec.makeMeasureSpec(min, MeasureSpec.EXACTLY)
        )
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mHeight = h
        mWeight = w
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        //绘制圆环
        canvas.drawCircle(mWeight / 2.0f, mHeight / 2.0f, abs(mWeight - 10) / 2.0f, mPaint)
        //绘制0°线
        canvas.drawLine(
            mWeight / 2.0f,
            mHeight / 2.0f,
            mWeight - 5.0f,
            mHeight / 2.0f,
            mPaint
        )

        if (mDeg != -1.0f) {
            //绘制角度线及角度标识
            canvas.save()
            canvas.translate(mWeight / 2.0f, mHeight / 2.0f)
            canvas.rotate(mDeg)
            canvas.drawLine(0.0f, 0.0f, abs(mWeight - 10) / 2.0f, 0.0f, mPaint)
            canvas.restore()
            canvas.drawArc(
                mWeight / 2.0f - 100,
                mHeight / 2.0f - 100,
                mWeight / 2.0f + 100,
                mHeight / 2.0f + 100,
                0.0f,
                mDeg,
                false,
                mPaint
            )
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                if (inCircleArea(event)) {
                    parent.requestDisallowInterceptTouchEvent(true)
                    calcDeg(event)
                } else {
                    return false
                }
            }
            MotionEvent.ACTION_MOVE -> {
                calcDeg(event)
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                parent.requestDisallowInterceptTouchEvent(false)
            }
            else -> {}
        }
        return true
    }

    private fun inCircleArea(event: MotionEvent): Boolean {
        val xInView = event.x
        val yInView = event.y
        val centerX = mWeight / 2.0f
        val centerY = mHeight / 2.0f

        val length =
            sqrt((xInView - centerX) * (xInView - centerX) + (yInView - centerY) * (yInView - centerY))

        return length <= (abs(mWeight - 10) / 2.0f)
    }

    private fun calcDeg(event: MotionEvent) {
        val xInView = event.x
        val yInView = event.y
        val deg = getRotationBetweenLines(mWeight / 2.0f, mHeight / 2.0f, xInView, yInView)
//        val deg = getAgree(mWeight / 2.0f, mHeight / 2.0f, xInView, yInView)
        if (deg != mDeg) {
            mDeg = deg
            callback?.onAngleChange(mDeg)
            invalidate()
        }
    }

    private fun getAgree(
        x: Float, y: Float, centerX: Float,
        centerY: Float
    ): Float {
        return if (x < centerX && y < centerY) {//a-180
            Math.toDegrees((atan(((centerY - y) / (centerX - x)).toDouble())))
                .toFloat() - 180
        } else if (x > centerX && y < centerY) {//-a
            -Math.toDegrees((atan(((centerY - y) / (x - centerX)).toDouble())))
                .toFloat()
        } else if (x < centerX && y > centerY) {//180-a
            180 - Math.toDegrees((atan(((y - centerY) / (centerX - x)).toDouble())))
                .toFloat()
        } else if (x > centerX && y > centerY) {//a
            Math.toDegrees((atan(((y - centerY) / (x - centerX)).toDouble())))
                .toFloat()
        } else
            0F

    }

    //获取从起点线到终点线经过的角度
    private fun getRotationBetweenLines(
        centerX: Float,
        centerY: Float,
        xInView: Float,
        yInView: Float
    ): Float {
        val x = xInView - centerX
        val y = yInView - centerY
        //计算斜边长度
        val z = sqrt((x * x + y * y).toDouble())
        //通过反正弦值和直角三角形的三角函数计算方式算出角度，再结合象限得到最终的角度
        val round = (asin(abs(y) / z) / Math.PI * 180).toFloat()
        //四个象限做处理
        return if (x < 0 && y > 0) {
            180 - round
        } else if (x < 0 && y < 0) {
            180 + round
        } else if (x > 0 && y < 0) {
            360 - round
        } else {
            round
        }
    }

    interface OnAngleChangeCallback {
        fun onAngleChange(angle: Float)
    }
}