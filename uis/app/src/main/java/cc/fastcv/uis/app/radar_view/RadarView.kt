package cc.fastcv.uis.app.radar_view

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import kotlin.math.sqrt

class RadarView : View {

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

    private var innerRadius = 80

    private val titles = arrayOf("力量", "体力", "物理防御", "魔法防御", "反应速度", "智力")
    private val strengths = arrayOf(1f, 0.2f, 0.2f, 0.2f, 0.2f, 0.2f)

    /**
     * 原始6个点
     * 顺时针方向
     */
    private val point0 = PointF(1f, 0f)
    private val point1 = PointF(0.5f, sqrt(0.75f))
    private val point2 = PointF(-0.5f, sqrt(0.75f))
    private val point3 = PointF(-1f, 0f * innerRadius)
    private val point4 = PointF(-0.5f, -sqrt(0.75f))
    private val point5 = PointF(0.5f, -sqrt(0.75f))

    private val path = Path().apply {
        moveTo(point0.x*innerRadius,point0.y*innerRadius)
        lineTo(point1.x*innerRadius,point1.y*innerRadius)
        lineTo(point2.x*innerRadius,point2.y*innerRadius)
        lineTo(point3.x*innerRadius,point3.y*innerRadius)
        lineTo(point4.x*innerRadius,point4.y*innerRadius)
        lineTo(point5.x*innerRadius,point5.y*innerRadius)
        close()
    }

    private val regionPath = Path()

    private val lineStrokeWidth = 2f

    private val paint = Paint().apply {
        color = Color.RED
        strokeWidth = lineStrokeWidth
        isAntiAlias = true
        style = Paint.Style.STROKE
    }

    private val titlePaint = Paint().apply {
        color = Color.BLUE
        isAntiAlias = true
        textSize = 30f
        strokeWidth = 3f
    }

    private val regionPaint = Paint().apply {
        color = Color.BLUE
        alpha = 127
        isAntiAlias = true
        strokeWidth = 3f
        style = Paint.Style.FILL_AND_STROKE
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawBgRadar(canvas)
        drawText(canvas)
        drawRegion(canvas)
    }

    private fun drawRegion(canvas: Canvas) {
        canvas.save()
        canvas.translate(width / 2.0f, height / 2.0f)
        regionPath.apply {
            reset()
            moveTo(point0.x*innerRadius*5f*strengths[0],point0.y*innerRadius*5f*strengths[0])
            lineTo(point1.x*innerRadius*5f*strengths[1],point1.y*innerRadius*5f*strengths[1])
            lineTo(point2.x*innerRadius*5f*strengths[2],point2.y*innerRadius*5f*strengths[2])
            lineTo(point3.x*innerRadius*5f*strengths[3],point3.y*innerRadius*5f*strengths[3])
            lineTo(point4.x*innerRadius*5f*strengths[4],point4.y*innerRadius*5f*strengths[4])
            lineTo(point5.x*innerRadius*5f*strengths[5],point5.y*innerRadius*5f*strengths[5])
            close()
        }
        canvas.drawPath(regionPath,regionPaint)
        canvas.restore()
    }

    private fun drawText(canvas: Canvas?) {
        canvas?.save()
        canvas?.translate(width / 2.0f, height / 2.0f)
        canvas?.drawText(titles[0], point0.x * innerRadius * 5f + 20, point0.y * innerRadius * 5f, titlePaint)
        canvas?.drawText(
            titles[1],
            point1.x * innerRadius * 5f,
            point1.y * innerRadius * 5f + 50,
            titlePaint
        )
        canvas?.drawText(
            titles[2],
            point2.x * innerRadius * 5f,
            point2.y * innerRadius * 5f + 50,
            titlePaint
        )
        canvas?.drawText(titles[3], point3.x * innerRadius * 5f - 80f, point3.y * innerRadius * 5f, titlePaint)
        canvas?.drawText(
            titles[4],
            point4.x * innerRadius * 5f,
            point4.y * innerRadius * 5f - 20,
            titlePaint
        )
        canvas?.drawText(
            titles[5],
            point5.x * innerRadius * 5f,
            point5.y * innerRadius * 5f - 20,
            titlePaint
        )
        canvas?.restore()
    }

    private fun drawBgRadar(canvas: Canvas?) {
        canvas?.save()
        canvas?.translate(width / 2.0f, height / 2.0f)
        canvas?.drawPath(path, paint)
        for (i in 1..4) {
            canvas?.save()
            canvas?.scale(1.0f + i, 1.0f + i)
            paint.strokeWidth = lineStrokeWidth / (1.0f + i)
            canvas?.drawPath(path, paint)
            canvas?.restore()
        }

        for (i in 0..5) {
            paint.strokeWidth = lineStrokeWidth
            canvas?.save()
            canvas?.rotate(i * 60f)
            canvas?.drawLine(0f, 0f, innerRadius * 5f, 0f, paint)
            canvas?.restore()
        }
        canvas?.restore()
    }


    fun setStrengths(strengths:FloatArray) {
        if (strengths.size != 6) {
            throw IllegalArgumentException("实力数据长度应该为6！！！")
        }

        strengths.forEachIndexed { index, fl ->
            this.strengths[index] = fl
        }
        invalidate()
    }
}