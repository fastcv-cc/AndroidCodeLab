package cc.fastcv.uis.app.bezier_curve

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import kotlin.math.abs

class QuadToView : View {

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

    companion object {
        private const val MAX_POINT = 3
    }

    private val path = Path()
    private var points = arrayOfNulls<PointF>(MAX_POINT)

    private var index = 0

    private var curTouchIndex = -1

    private val paint = Paint().apply {
        isAntiAlias = true
        color = Color.RED
        style = Paint.Style.STROKE
        strokeWidth = 10f
    }


    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                parent.requestDisallowInterceptTouchEvent(true)
                val touchIndex = getTouchPointIndex(event.x, event.y)
                Log.d("xcl_debug", "onTouchEvent: touchIndex = $touchIndex")
                if (touchIndex == -1) {
                    if (index < MAX_POINT) {
                        points[index] = PointF(event.x, event.y)
                        curTouchIndex = index
                        index++
                    } else {
                        parent.requestDisallowInterceptTouchEvent(false)
                        return super.onTouchEvent(event)
                    }
                } else {
                    curTouchIndex = touchIndex
                }
            }
            MotionEvent.ACTION_MOVE -> {
                points[curTouchIndex]!!.x = event.x
                points[curTouchIndex]!!.y = event.y
                invalidate()
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                parent.requestDisallowInterceptTouchEvent(false)
            }
            else -> {}
        }
        return true
    }

    /**
     * 返回 0 1 2 则表示对应的点
     * - 则表示不在任何点范围内
     */
    private fun getTouchPointIndex(x: Float, y: Float): Int {
        points.forEachIndexed { index, pointF ->
            if (pointF != null && (abs(x - pointF.x) < 50) && (abs(y - pointF.y) < 50)) {
                return index
            }
        }
        return -1
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        paint.strokeWidth = 20f
        for (point in points) {
            if (point != null) {
                canvas.drawPoint(point.x,point.y,paint)
            }
        }

        paint.strokeWidth = 10f
        if (index == MAX_POINT) {
            path.reset()
            path.moveTo(points[0]!!.x, points[0]!!.y)
            path.quadTo(points[1]!!.x, points[1]!!.y, points[2]!!.x, points[2]!!.y)
            canvas.drawPath(path, paint)
        }
    }
}