package cc.fastcv.simple_number_clock

abstract class AbsNumber {
    abstract val params: Array<CircleDrawParam>

    fun transition(newNumber: AbsNumber, progress: Float): TempNumber {
        return TempNumber(
            arrayOf(
                params[0].transition(newNumber.params[0],progress),
                params[1].transition(newNumber.params[1],progress),
                params[2].transition(newNumber.params[2],progress),
                params[3].transition(newNumber.params[3],progress),
                params[4].transition(newNumber.params[4],progress),
                params[5].transition(newNumber.params[5],progress),
            )
        )
    }
}