package cc.fastcv.uis.app.inscribed_circle

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.MotionEvent
import java.util.Random
import kotlin.math.abs
import kotlin.math.sqrt

class InscribedCircle(var cx: Float, var cy: Float, var radius: Float) {

    var color: Int = getRandomColor()

    fun draw(canvas: Canvas, paint: Paint) {
        paint.color = color
        paint.style = Paint.Style.FILL
        canvas.drawCircle(cx, cy, radius, paint)
    }

    fun inCircleArea(event: MotionEvent, totalW: Int, totalH: Int): Boolean {
        val xInView = event.x - totalW/2
        val yInView = event.y - totalH/2

        val length =
            sqrt((xInView - cx) * (xInView - cx) + (yInView - cy) * (yInView - cy))

        return length <= radius
    }

    private fun getRandomColor(): Int {
        val random = Random()
        return Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256))
    }
}