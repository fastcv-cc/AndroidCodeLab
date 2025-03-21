package cc.fastcv.uis.app.inscribed_circle

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.DashPathEffect
import android.graphics.Paint
import android.graphics.PathEffect
import android.os.Build
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.annotation.RequiresApi
import kotlin.math.PI
import kotlin.math.abs
import kotlin.math.asin
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt


class InscribedCircleView : View {
    constructor(context: Context?) : super(context) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }

    @SuppressLint("ObsoleteSdkInt")
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes) {
        init()
    }

    private var vHeight = 0
    private var vWeight = 0

    private var mL = 1000

    private var mN = 6

    private var mRadius = 0f

    private var mInscribedCircleRadius = 0f

    private val pathEffect: PathEffect = DashPathEffect(floatArrayOf(10f, 5f, 10f, 5f), 0f)

    private val paint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.STROKE
        strokeWidth = 5.0f
        color = Color.BLACK
    }


    private val circles = mutableListOf<InscribedCircle>()

    fun setLAndN(valueL: Int, valueN: Int) {
        mL = valueL
        mN = valueN
        init()
        invalidate()
    }

    private fun init() {
        circles.clear()
        paint.apply {
            style = Paint.Style.STROKE
            strokeWidth = 5.0f
            color = Color.BLACK
        }
        mRadius = mL / 4 / 2f
        val scale = abs(sin((((360f / (2 * mN)) * PI) / 180f)))
        mInscribedCircleRadius = ((scale * mL) / (8 * (1 - scale))).toFloat()
        val rA = 360f / mN
        val total = mRadius + mInscribedCircleRadius
        for (i in 0 until mN) {
            if (i == 0) {
                circles.add(InscribedCircle(total, 0f, mInscribedCircleRadius))
            } else {
                val sin = sin(rA * i * PI / 180f)
                val cos = cos(rA * i * PI / 180f)
                circles.add(
                    InscribedCircle(
                        (total * cos).toFloat(),
                        (total * sin).toFloat(),
                        mInscribedCircleRadius
                    )
                )
            }
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        vHeight = h
        vWeight = w
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        canvas.let {
            it.save()
            it.translate(vWeight / 2f, vHeight / 2f)
            //画正方形
            paint.pathEffect = pathEffect
            it.drawRect(-mRadius, mRadius, mRadius, -mRadius, paint)
            paint.pathEffect = null
            it.drawCircle(0f, 0f, mRadius, paint)
            paint.pathEffect = pathEffect
            it.drawLine(0f, 0f, vWeight / 2f, 0f, paint)
            val rA = 360f / mN
            for (i in 1 until mN) {
                it.rotate(rA)
                it.drawLine(0f, 0f, vWeight / 2f, 0f, paint)
            }
            it.restore()
            it.save()
            it.translate(vWeight / 2f, vHeight / 2f)
            paint.pathEffect = null
//            it.drawCircle(0f, mRadius + mInscribedCircleRadius, mInscribedCircleRadius, paint)
//            for (i in 1 until mN) {
//                it.rotate(rA)
//                it.drawCircle(0f, mRadius + mInscribedCircleRadius, mInscribedCircleRadius, paint)
//            }
            for (circle in circles) {
                circle.draw(it, paint)
            }
            it.restore()
        }

    }


    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                var handle = false
                for (circle in circles) {
                    if (circle.inCircleArea(event, vWeight, vHeight)) {
                        handle = true
                        parent.requestDisallowInterceptTouchEvent(true)
                        break
                    }
                }
                return handle
            }

            MotionEvent.ACTION_MOVE -> {
//                calcDeg(event)
            }

            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                parent.requestDisallowInterceptTouchEvent(false)
            }

            else -> {}
        }
        return true
    }


//    private fun calcDeg(event: MotionEvent) {
//        val xInView = event.x
//        val yInView = event.y
//        val deg = getRotationBetweenLines(mWeight / 2.0f, mHeight / 2.0f, xInView, yInView)
////        val deg = getAgree(mWeight / 2.0f, mHeight / 2.0f, xInView, yInView)
//        if (deg != mDeg) {
//            mDeg = deg
//            callback?.onAngleChange(mDeg)
//            invalidate()
//        }
//    }

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

}