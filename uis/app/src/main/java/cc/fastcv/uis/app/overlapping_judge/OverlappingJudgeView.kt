package cc.fastcv.uis.app.overlapping_judge

import android.annotation.SuppressLint
import android.app.Service
import android.content.Context
import android.graphics.*
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import kotlin.math.sqrt


class OverlappingJudgeView : View {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    private var vibrator = context.getSystemService(Service.VIBRATOR_SERVICE) as Vibrator?

    private val mRect = RectF(0.0f, 0.0f, 0.0f, 0.0f)

    private var mRadius = 100.0f

    private var mCircleCenterX = mRadius + 20.0f
    private var mCircleCenterY = mRadius + 20.0f

    private var centerX = 0.0f
    private var centerY = 0.0f

    private var mWidth = 0
    private var mHeight = 0

    private val mPaint = Paint().apply {
        color = Color.BLACK
        isAntiAlias = true
        style = Paint.Style.STROKE
        strokeWidth = 5.0f
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mWidth = w
        mHeight = h
        centerX = w / 2.0f
        centerY = h / 2.0f
        mRect.set(centerX - 100.0f, centerY - 200.0f, centerX + 100, centerY + 200)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawRect(mRect, mPaint)
        canvas.drawCircle(mCircleCenterX, mCircleCenterY, mRadius, mPaint)
    }

    /**
     * -1 代表未选中任何对象
     * 0 代表选中的矩形
     * 1 代表选中的圆形
     */
    private var target = -1

    private var initTouchX = 0.0f
    private var initTouchY = 0.0f

    private var isOverlapping = false

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                parent.requestDisallowInterceptTouchEvent(true)
                initTouchX = event.x
                initTouchY = event.y
                target = if (inRectArea(initTouchX, initTouchY)) {
                    0
                } else if (inCircleArea(initTouchX, initTouchY)) {
                    1
                } else {
                    -1
                }
            }
            MotionEvent.ACTION_MOVE -> {
                if (target == 0) {
                    moveRect(event)
                } else if (target == 1) {
                    moveCircle(event)
                }
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                parent.requestDisallowInterceptTouchEvent(false)
            }
            else -> {}
        }
        return true
    }

    private fun moveCircle(event: MotionEvent) {
        val dx = event.x - initTouchX
        val dy = event.y - initTouchY
        initTouchX = event.x
        initTouchY = event.y
        mCircleCenterX += dx
        mCircleCenterY += dy
        updateOverlappingStatus(judgeOverlapping(mRect, mCircleCenterX, mCircleCenterY))
        invalidate()
    }

    private fun moveRect(event: MotionEvent) {
        val dx = event.x - initTouchX
        val dy = event.y - initTouchY
        initTouchX = event.x
        initTouchY = event.y
        centerX += dx
        centerY += dy
        mRect.set(centerX - 100.0f, centerY - 200.0f, centerX + 100, centerY + 200)
        updateOverlappingStatus(judgeOverlapping(mRect, mCircleCenterX, mCircleCenterY))
        invalidate()
    }

    private fun judgeOverlapping(rect: Rect, circle: Rect) {
        judgeOverlapping(rect, circle.centerX(), circle.centerY())
    }

    private fun judgeOverlapping(rectF: RectF, circleF: RectF) {
        judgeOverlapping(rectF, circleF.centerX(), circleF.centerY())
    }

    private fun judgeOverlapping(rect: Rect, circleX: Int, circleY: Int) {
        judgeOverlapping(RectF(rect), circleX.toFloat(), circleY.toFloat())
    }

    private fun judgeOverlapping(rectF: RectF, circleX: Float, circleY: Float): Boolean {
        //首先通过x区分区域 左 中（圆心在线上） 右
        if (circleX < rectF.left) {
            //再通过y区分区域 上 中（圆心在线上） 下
            if (circleY < rectF.top) {
                //左上
                val length = sqrt(
                    (circleX - rectF.left) * (circleX - rectF.left)
                            + (circleY - rectF.top) * (circleY - rectF.top)
                )

                return length <= mRadius
            } else if (circleY >= rectF.top && circleY <= rectF.bottom) {
                //左中
                val length = rectF.left - circleX
                return length <= mRadius
            } else {
                //左下
                val length = sqrt(
                    (circleX - rectF.left) * (circleX - rectF.left)
                            + (circleY - rectF.bottom) * (circleY - rectF.bottom)
                )

                return length <= mRadius
            }
        } else if (circleX >= rectF.left && circleX <= rectF.right) {
            //再通过y区分区域 上 中（圆心在线上） 下
            return if (circleY < rectF.top) {
                //中上
                val length = rectF.top - circleY
                length <= mRadius
            } else if (circleY >= rectF.top && circleY <= rectF.bottom) {
                //中中 进入中中之前肯定接触了  不处理
                true
            } else {
                //中下
                val length = circleY - rectF.bottom
                length <= mRadius
            }
        } else {
            //再通过y区分区域 上 中（圆心在线上） 下
            if (circleY < rectF.top) {
                //右上
                val length = sqrt(
                    (circleX - rectF.right) * (circleX - rectF.right)
                            + (circleY - rectF.top) * (circleY - rectF.top)
                )

                return length <= mRadius
            } else if (circleY >= rectF.top && circleY <= rectF.bottom) {
                //右中
                val length = circleX - rectF.right
                return length <= mRadius
            } else {
                //右下
                val length = sqrt(
                    (circleX - rectF.right) * (circleX - rectF.right)
                            + (circleY - rectF.bottom) * (circleY - rectF.bottom)
                )

                return length <= mRadius
            }
        }
    }

    private fun updateOverlappingStatus(overlapping: Boolean) {
        if (overlapping != isOverlapping) {
            isOverlapping = overlapping
            if (isOverlapping) {
                mPaint.color = Color.RED
                if (vibrator?.hasVibrator() == true) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        vibrator!!.vibrate(
                            VibrationEffect.createOneShot(
                                1000,
                                VibrationEffect.DEFAULT_AMPLITUDE,
                            )
                        )

                        //间接性震动
//                        vibrator!!.vibrate(
//                            VibrationEffect.createWaveform(
//                                longArrayOf(
//                                    500, 1000, 500, 1000, 500, 1000
//                                ),
//                                0
//                            )
//                        )
                    } else {
                        vibrator!!.vibrate(longArrayOf(0, 1000, 0, 1000), 0)
                        //间接性震动
//                        vibrator!!.vibrate(longArrayOf(1000, 1000, 1000, 1000), 0)
                    }
                } else {
                    Log.e("TOP", "该设备没有震动器")
                }
            } else {
                mPaint.color = Color.BLACK
                if (vibrator?.hasVibrator() == true) {
                    vibrator!!.cancel()
                } else {
                    Log.e("TOP", "该设备没有震动器")
                }
            }
        }
    }

    private fun inRectArea(x: Float, y: Float): Boolean {
        return mRect.contains(x, y)
    }

    private fun inCircleArea(x: Float, y: Float): Boolean {
        val length =
            sqrt((x - mCircleCenterX) * (x - mCircleCenterX) + (y - mCircleCenterY) * (y - mCircleCenterY))

        return length <= mRadius
    }
}