package cc.fastcv.uis.app.round_corner_progress_bar

class Leaf {

    // 在绘制部分的位置
    var x: Float = 0f
    var y: Float = 0f

    // 控制叶子飘动的幅度
    var type: StartType = StartType.LITTLE

    // 旋转角度
    var rotateAngle = 0

    // 旋转方向--0代表顺时针，1代表逆时针
    var rotateDirection = 0

    // 起始时间(ms)
    var startTime = 0L
    override fun toString(): String {
        return "Leaf(x=$x, y=$y, type=$type, rotateAngle=$rotateAngle, rotateDirection=$rotateDirection, startTime=$startTime)"
    }


}