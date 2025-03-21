package cc.fastcv.uis.app.roundprogressbar

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator

class RoundProgressBar : View {

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

    private var mViewWidth = 0f
    private var mViewHeight = 0f
    private var mProgress = 0f

    private val styleLine = 0
    private val styleCircle = 1

    private var style = styleCircle

    private val mPaint = Paint().apply {
        color = Color.BLUE
        strokeJoin = Paint.Join.ROUND
        strokeCap = Paint.Cap.ROUND
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mViewWidth = w * 1.0f
        mViewHeight = h * 1.0f
        mPaint.strokeWidth = mViewHeight
    }

    fun run() {
        mProgress = 0f
        val anim = ValueAnimator.ofFloat(0f, 100f).apply {
            duration = 30000
            addUpdateListener {
                val value = it.animatedValue as Float
                mProgress = value
                interpolator = LinearInterpolator()
                invalidate()
            }
        }
        anim.start()
    }

    override fun draw(canvas: Canvas) {
        super.draw(canvas)
        //底部线
        mPaint.color = Color.GRAY
        canvas.drawLine(
            mViewHeight / 2,
            mViewHeight / 2,
            mViewWidth - mViewHeight / 2,
            mViewHeight / 2,
            mPaint
        )

        if (mProgress != 0f) {
            mPaint.color = Color.BLUE
            canvas.drawLine(
                mViewHeight / 2,
                mViewHeight / 2,
                mViewHeight / 2 + (mViewWidth - mViewHeight) * (mProgress / 100.0f),
                mViewHeight / 2,
                mPaint
            )
        }
    }
}