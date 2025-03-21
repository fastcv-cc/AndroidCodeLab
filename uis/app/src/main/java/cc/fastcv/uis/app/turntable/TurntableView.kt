package cc.fastcv.uis.app.turntable

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.animation.LinearInterpolator
import kotlin.math.*

class TurntableView : View {

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


    private var mMax = 0
    private var mMin = -180

    private var mViewHeight = 0.0f
    private var mViewWidth = 0.0f
    private var mCircleCenterPoint: PointF = PointF(0.0f, 0.0f)
    private var mOutStrokeWidth = 10.0f
    private var mOutPaint = Paint().apply {
        color = Color.parseColor("#59747A")
        strokeWidth = mOutStrokeWidth
        style = Paint.Style.STROKE
        isAntiAlias = true
    }

    private var mShaderPaint = Paint().apply {
        style = Paint.Style.FILL
        isAntiAlias = true
        shader = SweepGradient(0.0f, 0.0f, Color.parseColor("#85F606"), Color.parseColor("#85F606"))
        xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
    }

    private var mInnerStrokeWidth = 5.0f
    private var mInnerPaint = Paint().apply {
        color = Color.parseColor("#3B9A6A")
        strokeWidth = mInnerStrokeWidth
        style = Paint.Style.STROKE
        isAntiAlias = true
    }

    private var mScaleStrokeWidth = 4.0f
    private var mScalePaint = Paint().apply {
        color = Color.BLUE
        strokeWidth = mScaleStrokeWidth
        style = Paint.Style.FILL
        isAntiAlias = true
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mViewHeight = h * 1.0f
        mViewWidth = w * 1.0f
        mCircleCenterPoint.x = mViewWidth / 2.0f
        mCircleCenterPoint.y = mViewHeight / 2.0f
    }

    private var startDegrees = 0f

    private fun correctionAngle(startAngle:Float,endAngle:Float) {
        val anim = ValueAnimator.ofFloat(startAngle, endAngle).apply {
            duration = 250
            addUpdateListener {
                val value = it.animatedValue as Float
                interpolator = LinearInterpolator()
                startDegrees = value
                invalidate()
            }
        }
        anim.start()
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.let {
            it.save()

            it.translate(mCircleCenterPoint.x, mCircleCenterPoint.y)
            //画外圆
            it.drawCircle(
                0.0f,
                0.0f,
                (min(mViewWidth, mViewHeight) - mOutStrokeWidth) / 2f,
                mOutPaint
            )

            //画内圆
            it.drawCircle(
                0.0f,
                0.0f,
                (min(mViewWidth, mViewHeight) - mInnerStrokeWidth) / 2f - 100.0f,
                mInnerPaint
            )


            //绘制默认选中块颜色
            drawSelectAngle(it)


            //画刻度
//            for (i in 0..360 step 2) {
//                if (i == 0) {
//                    it.rotate(startDegrees)
//                } else {
//                    it.rotate(2.0f)
//                }
//                if (i % 20 == 0) {
//                    //画长刻度
//                    it.drawLine(
//                        (min(mViewWidth, mViewHeight) - mInnerStrokeWidth) / 2f - 80.0f,
//                        0f,
//                        (min(mViewWidth, mViewHeight) - mOutStrokeWidth) / 2f - 20f,
//                        0f,
//                        mScalePaint
//                    )
//                    continue
//                }
//
//                if (i % 10 == 0) {
//                    //画短刻度
//                    it.drawLine(
//                        (min(mViewWidth, mViewHeight) - mInnerStrokeWidth) / 2f - 80.0f,
//                        0f,
//                        (min(mViewWidth, mViewHeight) - mOutStrokeWidth) / 2f - 35f,
//                        0f,
//                        mScalePaint
//                    )
//
//                    continue
//                }
//
//                //画普通刻度
//                it.drawLine(
//                    (min(mViewWidth, mViewHeight) - mInnerStrokeWidth) / 2f - 80.0f,
//                    0f,
//                    (min(mViewWidth, mViewHeight) - mOutStrokeWidth) / 2f - 50f,
//                    0f,
//                    mScalePaint
//                )
//            }
            it.restore()
        }
    }

    private fun drawSelectAngle(canvas: Canvas) {
        val saved = canvas.saveLayer(null, null)

        //画刻度
        for (i in 0..359 step 2) {
            if (i == 0) {
                canvas.rotate(startDegrees)
            } else {
                canvas.rotate(2.0f)
            }

            if (i == 0) {
                mScalePaint.color = Color.BLUE
                //画指示长刻度
                canvas.drawLine(
                    (min(mViewWidth, mViewHeight) - mInnerStrokeWidth) / 2f - 80.0f,
                    0f,
                    (min(mViewWidth, mViewHeight) - mOutStrokeWidth) / 2f - 10f,
                    0f,
                    mScalePaint
                )
                mScalePaint.color = Color.parseColor("#64747A")
                continue
            }

            if (i % 20 == 0) {
                //画长刻度
                canvas.drawLine(
                    (min(mViewWidth, mViewHeight) - mInnerStrokeWidth) / 2f - 80.0f,
                    0f,
                    (min(mViewWidth, mViewHeight) - mOutStrokeWidth) / 2f - 20f,
                    0f,
                    mScalePaint
                )
                continue
            }

            if (i % 10 == 0) {
                //画短刻度
                canvas.drawLine(
                    (min(mViewWidth, mViewHeight) - mInnerStrokeWidth) / 2f - 80.0f,
                    0f,
                    (min(mViewWidth, mViewHeight) - mOutStrokeWidth) / 2f - 35f,
                    0f,
                    mScalePaint
                )

                continue
            }

            //画普通刻度
            canvas.drawLine(
                (min(mViewWidth, mViewHeight) - mInnerStrokeWidth) / 2f - 80.0f,
                0f,
                (min(mViewWidth, mViewHeight) - mOutStrokeWidth) / 2f - 50f,
                0f,
                mScalePaint
            )
        }
        val r = (min(mViewWidth, mViewHeight) - mOutStrokeWidth) / 2f
        //画外圆渐变色
        canvas.drawArc(
            -r, -r, r, r,
            225f - startDegrees,
            10f,
            true,
            mShaderPaint
        )

        canvas.restoreToCount(saved)
    }

    private var downDegrees = 0f

    private var isUselessEvent = false

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                if (inCircleArea(event)) {
                    isUselessEvent = false
                    parent.requestDisallowInterceptTouchEvent(true)
                    downDegrees = calcDeg(event)
                } else {
                    return false
                }
            }
            MotionEvent.ACTION_MOVE -> {
                //获取的值是 0- 360
                if (inCircleArea(event) && !isUselessEvent) {
                    val curDeg = calcDeg(event)
                    var dDeg = curDeg - downDegrees
                    if (abs(dDeg) > 300) {
                        //存在0-360的转变
                        dDeg = (360 - abs(dDeg))*(dDeg/300)
                    }
                    downDegrees = curDeg

                    if (startDegrees >= mMax) {
                        dDeg *= (100 - startDegrees) / 100.0f
                    }

                    if (startDegrees <= mMin) {
                        dDeg *= (100 - (mMin - startDegrees)) / 100.0f
                    }

                    startDegrees += dDeg
                    if (!startDegrees.isNaN()) {
                        invalidate()
                    }
                } else {
                    isUselessEvent = true
                }
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                parent.requestDisallowInterceptTouchEvent(false)
                if (!startDegrees.isNaN()) {
                    var fl: Int = if (startDegrees >= mMax) {
                        mMax
                    } else if (startDegrees <= mMin) {
                        mMin
                    } else {
                        startDegrees.roundToInt()
                    }

                    if (fl % 2 != 0) {
                        val dx = startDegrees - fl
                        if (dx < 0.0) {
                            fl += 1
                        } else {
                            fl -= 1
                        }
                    }
                    correctionAngle(startDegrees,fl*1.0f)
                    startDegrees = fl*1.0f
                }
            }
            else -> {}
        }
        return true

    }

    private fun calcDeg(event: MotionEvent): Float {
        val xInView = event.x
        val yInView = event.y
        return getRotationBetweenLines(mCircleCenterPoint.x, mCircleCenterPoint.y, xInView, yInView)
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

    private fun inCircleArea(event: MotionEvent): Boolean {
        val xInView = event.x
        val yInView = event.y
        val centerX = mCircleCenterPoint.x
        val centerY = mCircleCenterPoint.y

        val length =
            sqrt((xInView - centerX) * (xInView - centerX) + (yInView - centerY) * (yInView - centerY))

//        return length <= (min(mViewWidth, mViewHeight) / 2f) && length >= 150
        return length >= 150
    }

}