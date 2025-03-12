package cc.fastcv.simple_number_clock

import android.graphics.Canvas

/**
 * 3个点
 */
class ClockSplit(
    private val array: Array<SimpleClockCircle>,
) {

    fun draw(canvas: Canvas) {
        for ((index, circle) in array.withIndex()) {
            circle.drawBaseCircle(canvas)
            if (index == 1) {
                circle.drawBlackPoint(canvas)
            }
        }
    }

}