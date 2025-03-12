package cc.fastcv.simple_number_clock

import android.graphics.Canvas

/**
 * 6个点
 */
class SingleNumber(
    private val array: Array<Array<SimpleClockCircle>>,
    private val number: AbsNumber
) {

    fun draw(canvas: Canvas) {
        for ((index, minimalUhrCircles) in array.withIndex()) {
            for ((index1, minimalUhrCircle) in minimalUhrCircles.withIndex()) {
                minimalUhrCircle.drawBaseCircle(canvas)
                minimalUhrCircle.drawByCircleDrawParam(canvas, number.params[index * 2 + index1])
            }
        }
    }

}