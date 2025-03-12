package cc.fastcv.simple_number_clock


class TempNumber(override val params: Array<CircleDrawParam>) : AbsNumber()

class Number0 : AbsNumber() {
    override val params = arrayOf(
        CircleDrawParam(0f, (1f * 255).toInt(), 90f, (1f * 255).toInt()),
        CircleDrawParam(90f, (1f * 255).toInt(), 180f, (1f * 255).toInt()),
        CircleDrawParam(90f, (1f * 255).toInt(), -90f, (1f * 255).toInt()),
        CircleDrawParam(90f, (1f * 255).toInt(), -90f, (1f * 255).toInt()),
        CircleDrawParam(0f, (1f * 255).toInt(), -90f, (1f * 255).toInt()),
        CircleDrawParam(180f, (1f * 255).toInt(), -90f, (1f * 255).toInt())
    )
}

class Number1 : AbsNumber() {
    override val params = arrayOf(
        CircleDrawParam(-90f, (0f * 255).toInt(),-90f, (0f * 255).toInt()),
        CircleDrawParam(90f, (1f * 255).toInt(),135f, (1f * 255).toInt()),
        CircleDrawParam(-90f, (0f * 255).toInt(),-90f, (0f * 255).toInt()),
        CircleDrawParam(90f, (1f * 255).toInt(),-90f, (1f * 255).toInt()),
        CircleDrawParam(-90f, (0f * 255).toInt(),-90f, (0f * 255).toInt()),
        CircleDrawParam(-90f, (0f * 255).toInt(),-90f, (1f * 255).toInt())
    )
}

class Number2 : AbsNumber() {
    override val params = arrayOf(
        CircleDrawParam(0f, (1f * 255).toInt(),0f, (1f * 255).toInt()),
        CircleDrawParam(90f, (1f * 255).toInt(),180f, (1f * 255).toInt()),
        CircleDrawParam(0f, (1f * 255).toInt(),90f, (1f * 255).toInt()),
        CircleDrawParam(-90f, (1f * 255).toInt(),180f, (1f * 255).toInt()),
        CircleDrawParam(-90f, (1f * 255).toInt(),0f, (1f * 255).toInt()),
        CircleDrawParam(180f, (1f * 255).toInt(),180f, (1f * 255).toInt())
    )
}

class Number3 : AbsNumber() {
    override val params = arrayOf(
        CircleDrawParam(0f, (1f * 255).toInt(),0f, (1f * 255).toInt()),
        CircleDrawParam(90f, (1f * 255).toInt(),180f, (1f * 255).toInt()),
        CircleDrawParam(0f, (1f * 255).toInt(),0f, (1f * 255).toInt()),
        CircleDrawParam(-90f, (1f * 255).toInt(),90f, (1f * 255).toInt()),
        CircleDrawParam(0f, (1f * 255).toInt(),0f, (1f * 255).toInt()),
        CircleDrawParam(180f, (1f * 255).toInt(),-90f, (1f * 255).toInt())
    )
}

class Number4 : AbsNumber() {
    override val params = arrayOf(
        CircleDrawParam(90f, (1f * 255).toInt(),90f, (1f * 255).toInt()),
        CircleDrawParam( 90f, (1f * 255).toInt(),90f, (1f * 255).toInt()),
        CircleDrawParam( -90f, (1f * 255).toInt(),0f, (1f * 255).toInt()),
        CircleDrawParam( 90f, (1f * 255).toInt(),-90f, (1f * 255).toInt()),
        CircleDrawParam( -90f, (0f * 255).toInt(),-90f, (0f * 255).toInt()),
        CircleDrawParam( -90f, (1f * 255).toInt(),-90f, (1f * 255).toInt()),
    )
}

class Number5 : AbsNumber() {
    override val params = arrayOf(
        CircleDrawParam( 0f, (1f * 255).toInt(),90f, (1f * 255).toInt()),
        CircleDrawParam( 180f, (1f * 255).toInt(),180f, (1f * 255).toInt()),
        CircleDrawParam( -90f, (1f * 255).toInt(),0f, (1f * 255).toInt()),
        CircleDrawParam( 90f, (1f * 255).toInt(),180f, (1f * 255).toInt()),
        CircleDrawParam( 0f, (1f * 255).toInt(),0f, (1f * 255).toInt()),
        CircleDrawParam( -90f, (1f * 255).toInt(),180f, (1f * 255).toInt()),
    )
}

class Number6 : AbsNumber() {
    override val params = arrayOf(
        CircleDrawParam( 0f, (1f * 255).toInt(),90f, (1f * 255).toInt()),
        CircleDrawParam( 180f, (1f * 255).toInt(),180f, (1f * 255).toInt()),
        CircleDrawParam( 90f, (1f * 255).toInt(),-90f, (1f * 255).toInt()),
        CircleDrawParam( 90f, (1f * 255).toInt(),180f, (1f * 255).toInt()),
        CircleDrawParam( -90f, (1f * 255).toInt(),0f, (1f * 255).toInt()),
        CircleDrawParam( -90f, (1f * 255).toInt(),180f, (1f * 255).toInt()),
    )
}

class Number7 : AbsNumber() {
    override val params = arrayOf(
        CircleDrawParam( 0f, (1f * 255).toInt(),0f, (1f * 255).toInt()),
        CircleDrawParam( 90f, (1f * 255).toInt(),180f, (1f * 255).toInt()),
        CircleDrawParam( -90f, (0f * 255).toInt(),-90f, (0f * 255).toInt()),
        CircleDrawParam( 90f, (1f * 255).toInt(),-90f, (1f * 255).toInt()),
        CircleDrawParam( -90f, (0f * 255).toInt(),-90f, (0f * 255).toInt()),
        CircleDrawParam( -90f, (1f * 255).toInt(),-90f, (1f * 255).toInt()),
    )
}

class Number8 : AbsNumber() {
    override val params = arrayOf(
        CircleDrawParam( 0f, (1f * 255).toInt(),90f, (1f * 255).toInt()),
        CircleDrawParam( 90f, (1f * 255).toInt(),180f, (1f * 255).toInt()),
        CircleDrawParam( 0f, (1f * 255).toInt(),0f, (1f * 255).toInt()),
        CircleDrawParam( 180f, (1f * 255).toInt(),180f, (1f * 255).toInt()),
        CircleDrawParam( -90f, (1f * 255).toInt(),0f, (1f * 255).toInt()),
        CircleDrawParam( -90f, (1f * 255).toInt(),180f, (1f * 255).toInt()),
    )
}

class Number9 : AbsNumber() {
    override val params = arrayOf(
        CircleDrawParam( 0f, (1f * 255).toInt(),90f, (1f * 255).toInt()),
        CircleDrawParam( 90f, (1f * 255).toInt(),180f, (1f * 255).toInt()),
        CircleDrawParam( 0f, (1f * 255).toInt(),-90f, (1f * 255).toInt()),
        CircleDrawParam( 90f, (1f * 255).toInt(),-90f, (1f * 255).toInt()),
        CircleDrawParam( 0f, (1f * 255).toInt(),0f, (1f * 255).toInt()),
        CircleDrawParam( -90f, (1f * 255).toInt(),180f, (1f * 255).toInt()),
    )
}