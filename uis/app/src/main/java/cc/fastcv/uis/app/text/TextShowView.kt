package cc.fastcv.uis.app.text

import android.content.Context
import android.graphics.Camera
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.View

class TextShowView : View {

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

    var camera = Camera()

    var maxSize = 9

    var startAngle = 90 % maxSize/2
    var endAngle = 180 - startAngle
    var intervalAngle = 90 / maxSize/2

    private val paint = Paint().apply {
        style = Paint.Style.FILL_AND_STROKE
        color = Color.RED
        textSize = 40f
        textAlign = Paint.Align.CENTER
    }

    private var texts = arrayListOf<String>().apply {
        add("1")
        add("2")
        add("3")
        add("4")
        add("5")
        add("6")
        add("7")
        add("8")
        add("9")
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        val fontMetrics: Paint.FontMetrics = paint.fontMetrics
        Log.d(
            "xcl_debug",
            "onSizeChanged: baseline的距离是 ${fontMetrics.top}  下面的距离是 ${fontMetrics.bottom}"
        )
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.let {
            it.save()
            it.translate(width / 2.0f, height / 2.0f)
            drawAxis(it)
            drawTexts(it)
            it.restore()
        }
    }

    private fun drawTexts(it: Canvas) {
        paint.color = Color.RED
        val totalHeight = maxSize*getTextHeight()
        for (i in 0 until maxSize) {
            camera.save()
            it.drawText(
                texts[i],
                0f,
                getTextYCenterOffset(),
                paint
            )
            camera.rotateX((i*intervalAngle).toFloat())
            camera.restore()
        }
    }

    private fun drawAxis(it: Canvas) {
        paint.color = Color.BLACK
        it.drawLine(-width / 2.0f, 0f, width / 2.0f, 0f, paint)
        it.drawLine(0f, -height / 2.0f, 0f, height / 2.0f, paint)
    }

    private fun getTextYCenterOffset(): Float {
        return -paint.fontMetrics.top - (paint.fontMetrics.bottom - paint.fontMetrics.top) / 2
    }

    private fun getTextYLeftTopOffset(): Float {
        return -paint.fontMetrics.top
    }

    private fun getTextHeight(): Float {
        return paint.fontMetrics.bottom - paint.fontMetrics.top
    }

    private fun getTextWidth(text:String): Float {
        return paint.measureText(text)
    }

}