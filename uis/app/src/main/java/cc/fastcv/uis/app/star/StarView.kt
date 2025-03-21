package cc.fastcv.uis.app.star

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin
import kotlin.math.sqrt

class StarView : View {

    constructor(context: Context?) : super(context)

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    private var mViewWidth = 0.0f
    private var mViewHeight = 0.0f
    private var mStarOuterRadius = 100f
    private var mStarInnerRadius = 70f
    private val mPath = Path()

    private val starPoints = mutableListOf<PointF>()

    private val mPaint = Paint().apply {
        color = Color.BLACK
        isAntiAlias = true
        style = Paint.Style.FILL
        strokeWidth = 10.0f
        strokeJoin = Paint.Join.MITER
    }

    private fun initStar(outerRadius: Float, innerRadius: Float) {
        //获取外部5个点
        var reg = angleToRadian(54.0f)
        val s1 = PointF((cos(reg) * outerRadius), (sin(reg) * outerRadius))
        reg = angleToRadian(126.0f)
        val s2 = PointF((cos(reg) * outerRadius), (sin(reg) * outerRadius))
        reg = angleToRadian(198.0f)
        val s3 = PointF((cos(reg) * outerRadius), (sin(reg) * outerRadius))
        reg = angleToRadian(270.0f)
        val s4 = PointF((cos(reg) * outerRadius), (sin(reg) * outerRadius))
        reg = angleToRadian(342.0f)
        val s5 = PointF((cos(reg) * outerRadius), (sin(reg) * outerRadius))

        //获取内部5个点
        reg = angleToRadian(18.0f)
        val n1 = PointF((cos(reg) * innerRadius), (sin(reg) * innerRadius))
        reg = angleToRadian(90.0f)
        val n2 = PointF((cos(reg) * innerRadius), (sin(reg) * innerRadius))
        reg = angleToRadian(162.0f)
        val n3 = PointF((cos(reg) * innerRadius), (sin(reg) * innerRadius))
        reg = angleToRadian(234.0f)
        val n4 = PointF((cos(reg) * innerRadius), (sin(reg) * innerRadius))
        reg = angleToRadian(306.0f)
        val n5 = PointF((cos(reg) * innerRadius), (sin(reg) * innerRadius))

        starPoints.clear()
        starPoints.add(n1)
        starPoints.add(s1)
        starPoints.add(n2)
        starPoints.add(s2)
        starPoints.add(n3)
        starPoints.add(s3)
        starPoints.add(n4)
        starPoints.add(s4)
        starPoints.add(n5)
        starPoints.add(s5)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mViewWidth = w * 1.0f
        mViewHeight = h * 1.0f
        mStarOuterRadius = (mViewWidth - 100) / 2.0f
        mStarInnerRadius = (mViewWidth - 100) / 2.0f / 2.0f
        initStar(mStarOuterRadius, mStarInnerRadius)
    }

    override fun draw(canvas: Canvas) {
        super.draw(canvas)
        mPath.reset()
        starPoints.forEachIndexed { index, pointF ->
            if (index == 0) {
                mPath.moveTo(pointF.x,pointF.y)
            } else {
                mPath.lineTo(pointF.x,pointF.y)
            }
        }
        mPath.close()

        canvas.save()
        canvas.translate(mViewWidth / 2.0f, mViewHeight / 2.0f)
        canvas.drawPath(mPath, mPaint)
        canvas.restore()
    }

    /**
     * 角度转弧度，由于Math的三角函数需要传入弧度制，而不是角度值，所以要角度换算为弧度，角度 / 180 * π
     *
     * @param angle 角度
     * @return 弧度
     */
    private fun angleToRadian(angle: Float): Float {
        return (angle / 180f * Math.PI).toFloat()
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                if (isTouchOuterPointer(event.x, event.y)) {
                    parent.requestDisallowInterceptTouchEvent(true)
                    return true
                }
                return super.onTouchEvent(event)
            }
            MotionEvent.ACTION_MOVE -> {
                val radius =
                    sqrt((event.x - mViewWidth / 2.0f) * (event.x - mViewWidth / 2.0f) + (event.y - mViewHeight / 2.0f) * (event.y - mViewHeight / 2.0f))
                initStar(mStarOuterRadius, min(radius,mStarOuterRadius))
                invalidate()
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                parent.requestDisallowInterceptTouchEvent(false)
            }
            else -> {}
        }
        return true
    }

    private fun isTouchOuterPointer(x: Float, y: Float): Boolean {
        val fixX = x - mViewWidth / 2.0f
        val fixY = y - mViewHeight / 2.0f
        var result = false
        starPoints.forEachIndexed { index, pointF ->
            if (index % 2 == 0) {
                if (fixX < pointF.x + 50 && fixX > pointF.x - 50 && fixY < pointF.y + 50 && fixY > pointF.y - 50) {
                    result = true
                    return@forEachIndexed
                }
            }
        }
        return result
    }

}