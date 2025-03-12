package cc.fastcv.simple_number_clock

import android.graphics.Canvas

enum class ShowGroupType {
    TIME, SPLIT
}

class SimpleClockCircleGroup(
    private val array: Array<Array<SimpleClockCircle>>,
    private val type: ShowGroupType
) {

    fun draw(canvas: Canvas, number: Int, progress: Float) {
        if (type == ShowGroupType.TIME) {
            drawNumber(canvas, number, progress)
        } else {
            drawSplit(canvas)
        }
    }

    private fun drawSplit(canvas: Canvas) {
        val clockSplit = ClockSplit(
            arrayOf(
                array[0][0], array[1][0], array[2][0]
            )
        )
        clockSplit.draw(canvas)
    }

    private var lastLeftNumber: AbsNumber? = null
    private var lastRightNumber: AbsNumber? = null

    private fun drawNumber(canvas: Canvas, number: Int, progress: Float) {
        val newLeftNumber = transformNumber(number / 10)
        val newRightNumber = transformNumber(number % 10)
        if (progress == 1f) {
            lastLeftNumber = newLeftNumber
            lastRightNumber = newRightNumber
        }
        val leftTempNumber: AbsNumber = lastLeftNumber!!.transition(newLeftNumber!!, progress)
        val rightTempNumber: AbsNumber = lastRightNumber!!.transition(newRightNumber!!, progress)
        val leftNumber = SingleNumber(
            arrayOf(
                arrayOf(array[0][0], array[0][1]),
                arrayOf(array[1][0], array[1][1]),
                arrayOf(array[2][0], array[2][1]),
            ), leftTempNumber
        )
        leftNumber.draw(canvas)
        val rightNumber = SingleNumber(
            arrayOf(
                arrayOf(array[0][2], array[0][3]),
                arrayOf(array[1][2], array[1][3]),
                arrayOf(array[2][2], array[2][3]),
            ), rightTempNumber
        )
        rightNumber.draw(canvas)

    }

    private fun transformNumber(i: Int): AbsNumber? {
        return when (i) {
            0 -> Number0()
            1 -> Number1()
            2 -> Number2()
            3 -> Number3()
            4 -> Number4()
            5 -> Number5()
            6 -> Number6()
            7 -> Number7()
            8 -> Number8()
            9 -> Number9()
            else -> null
        }
    }

}