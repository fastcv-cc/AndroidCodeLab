package cc.fastcv.uis.app.round_corner_progress_bar

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.animation.LinearInterpolator
import cc.fastcv.uis.app.R
import java.util.*
import kotlin.math.acos
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sin

class LeafLoadingView : View {

    constructor(context: Context?) : super(context) {
        init(context, null)
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init(context, attrs)
    }

    companion object {
        // 中等振幅大小
        private const val MIDDLE_AMPLITUDE = 20

        // 不同类型之间的振幅差距
        private const val AMPLITUDE_DISPARITY = 20

        // 叶子飘动一个周期所花的时间
        private const val LEAF_FLOAT_TIME: Long = 3000

        // 叶子旋转一周需要的时间
        private const val LEAF_ROTATE_TIME: Long = 2000
    }

    private var leafBitmap = BitmapFactory.decodeResource(resources, R.drawable.ic_leaf)

    private val factory = LeafFactory()

    private lateinit var leafs: List<Leaf>

    // 中等振幅大小
    private val mMiddleAmplitude = MIDDLE_AMPLITUDE

    // 振幅差
    private val mAmplitudeDisparity = AMPLITUDE_DISPARITY

    // 叶子飘动一个周期所花的时间
    private val mLeafFloatTime: Long = LEAF_FLOAT_TIME

    // 叶子旋转一周需要的时间
    private val mLeafRotateTime: Long = LEAF_ROTATE_TIME

    private fun init(context: Context?, attrs: AttributeSet?) {
        leafs = factory.generateLeafs()
        Log.d("xcl_debug", "init: ${leafs.joinToString()}")
    }

    private var viewHeight = 0f
    private var viewWidth = 0f

    private var curProgress = 0f
    private var totalProgress = 100f

    /**
     * 外圈的圆角矩形
     */
    private var outRect = RectF()

    private var outPaint = Paint().apply {
        isAntiAlias = true
        color = Color.parseColor("#fce49b")
        style = Paint.Style.FILL
    }

    /**
     * 进入条画笔
     */
    private var padding = 10f
    private var innerLeftRect = RectF()
    private var innerRightRect = RectF()
    private var innerPaint = Paint().apply {
        isAntiAlias = true
        color = Color.parseColor("#f5a418")
        style = Paint.Style.FILL
    }


    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        viewHeight = h.toFloat()
        viewWidth = w.toFloat()
        outRect.left = 0f
        outRect.top = 0f
        outRect.right = viewWidth
        outRect.bottom = viewHeight

        innerLeftRect.left = 0f + padding
        innerLeftRect.top = 0f + padding
        innerLeftRect.right = viewHeight - padding
        innerLeftRect.bottom = viewHeight - padding

        innerRightRect.left = viewWidth - viewHeight + padding
        innerRightRect.top = 0f + padding
        innerRightRect.right = viewWidth - padding
        innerRightRect.bottom = viewHeight - padding
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        //绘制外圈原型矩形
        canvas.drawRoundRect(outRect, viewHeight / 2.0f, viewHeight / 2.0f, outPaint)
        val drawWidth = curProgress / totalProgress * viewWidth
        val radius = viewHeight / 2.0f

        if (drawWidth <= viewHeight / 2.0f) {
            //如果进入在左边圆弧内
            // 单边角度
            val angle = Math.toDegrees(acos((radius - drawWidth) / radius).toDouble()).toFloat()
            drawLeftArc(180f - angle, 180 + angle, canvas, innerPaint)
        } else if (drawWidth <= viewWidth - radius) {
            //画圆弧
            drawLeftArc(90f, 270f, canvas, innerPaint)
            //画矩形
            canvas.drawRect(radius, 0f + padding, drawWidth, viewHeight - padding, innerPaint)
        } else {
            //画圆弧
            drawLeftArc(90f, 270f, canvas, innerPaint)
            //画矩形
            canvas.drawRect(
                radius,
                0f + padding,
                viewWidth - radius,
                viewHeight - padding,
                innerPaint
            )
            //画右侧圆弧
            drawRightArc(-90f, 90f, canvas, innerPaint)
            // 画未填充部分
            val angle = Math.toDegrees(acos((radius - (viewWidth - drawWidth)) / radius).toDouble())
                .toFloat()
            drawRightArc(-angle, angle, canvas, outPaint)
        }

        drawLeaf(canvas)
    }

    private fun drawLeftArc(startAngle: Float, endAngle: Float, canvas: Canvas?, paint: Paint) {
        canvas?.drawArc(innerLeftRect, startAngle, endAngle - startAngle, false, paint)
    }

    private fun drawRightArc(startAngle: Float, endAngle: Float, canvas: Canvas?, paint: Paint) {
        canvas?.drawArc(innerRightRect, startAngle, endAngle - startAngle, false, paint)
    }

    fun run() {
        curProgress = 0f
        val anim = ValueAnimator.ofFloat(0f, 100f).apply {
            duration = 20000
            addUpdateListener {
                val value = it.animatedValue as Float
                curProgress = value
                interpolator = LinearInterpolator()
                invalidate()
            }
        }
        anim.start()
    }

    private fun drawLeaf(canvas: Canvas?) {
        val currentTimeMillis = System.currentTimeMillis()
        for (leaf in leafs) {
            //根据时间计算 x y 的位置
            val intervalTime = currentTimeMillis - leaf.startTime

            if (intervalTime <= 0) {
                continue
            } else if (intervalTime >= mLeafFloatTime) {
                leaf.startTime =
                    System.currentTimeMillis() + Random().nextInt(mLeafFloatTime.toInt())
            }
            val fraction: Float = min(1.0f, intervalTime.toFloat() / mLeafFloatTime)
            leaf.x = max((viewWidth - viewWidth * fraction).toInt().toFloat(), padding)
            leaf.y = getLocationY(leaf)

            canvas?.save()
            val matrix = Matrix()
            matrix.postTranslate(leaf.x, leaf.y)
            canvas?.drawBitmap(leafBitmap, matrix, outPaint)
            canvas?.restore()
        }
    }

    // 通过叶子信息获取当前叶子的Y值
    private fun getLocationY(leaf: Leaf): Float {
        // y = A(wx+Q)+h
        val w: Float = (2f * Math.PI / viewWidth).toFloat()
        var a = mMiddleAmplitude.toFloat()
        when (leaf.type) {
            StartType.LITTLE ->                 // 小振幅 ＝ 中等振幅 － 振幅差
                a = (mMiddleAmplitude - mAmplitudeDisparity).toFloat()

            StartType.MIDDLE -> a = mMiddleAmplitude.toFloat()
            StartType.BIG ->                 // 小振幅 ＝ 中等振幅 + 振幅差
                a = (mMiddleAmplitude + mAmplitudeDisparity).toFloat()

            else -> {}
        }
        return (a * sin((w * leaf.x).toDouble())).toInt() + viewHeight / 3.0f
    }

}