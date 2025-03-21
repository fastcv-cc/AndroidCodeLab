package cc.fastcv.uis.app.path_measure

import android.animation.Animator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.View
import kotlin.math.max
import kotlin.math.min

class SearchLoadingView : View {

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    private val circlePath = Path()
    private val searchPath = Path()
    private val circlePathDst = Path()
    private val searchPathDst = Path()
    private var circleCurProcess = 0f
    private var searchCurProcess = 0f

    private val circlePathMeasure = PathMeasure(circlePath, true)
    private val searchPathMeasure = PathMeasure(searchPath, true)

    private val circleMaxRange = 350f

    private var padding = 30

    private var state = State.IDLE

    private var preFinish = false

    private val paint = Paint().apply {
        color = Color.RED
        strokeWidth = 10f
        style = Paint.Style.STROKE
        isAntiAlias = true
        strokeJoin = Paint.Join.ROUND
        strokeCap = Paint.Cap.ROUND
    }

    private val defaultDuration = 1000L

    private val searchingAnim = ValueAnimator.ofFloat(0f, 100f)

    private val prepareAnim = ValueAnimator.ofFloat(0f, 100f)

    fun startSearch() {
        if (state == State.IDLE) {
            state = State.PREPARE
            prepareAnim.start()
        }
    }

    fun finishSearch() {
        preFinish = true
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        val circleRadius = min(w / 2f, h / 2f) - padding
        circlePath.reset()
        circlePath.moveTo(0f, circleRadius)
        circlePath.addArc(-circleRadius, -circleRadius, circleRadius, circleRadius, 90f, -359.9f)
        circlePath.close()
        circlePathMeasure.setPath(circlePath, false)


        searchPath.reset()
        searchPath.moveTo(0f, circleRadius / 2f)
        searchPath.addArc(
            -circleRadius / 2f,
            -circleRadius / 2f,
            circleRadius / 2f,
            circleRadius / 2f,
            90f,
            359.9f
        )
        searchPath.lineTo(0f, circleRadius / 2f)
        searchPath.lineTo(0f, circleRadius)
        searchPathMeasure.setPath(searchPath, false)

        searchingAnim.apply {
            duration = defaultDuration
            addUpdateListener {
                circleCurProcess = it.animatedValue as Float
                invalidate()
            }
            addListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(animation: Animator) {}

                override fun onAnimationEnd(animation: Animator) {
                    //判断状态是否完成
                    if (preFinish) {
                        //进入完成状态 即准备状态倒过来
                        state = State.FINISHED
                        prepareAnim.reverse()
                    } else {
                        animation.start()
                    }
                }

                override fun onAnimationCancel(animation: Animator) {}

                override fun onAnimationRepeat(animation: Animator) {}

            })
        }

        prepareAnim.apply {
            duration = defaultDuration
            addUpdateListener {
                searchCurProcess = it.animatedValue as Float
                invalidate()
            }
            addListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(animation: Animator) {}

                override fun onAnimationEnd(animation: Animator) {
                    if (state == State.FINISHED) {
                        //重置状态
                        state = State.IDLE
                        preFinish = false
                        invalidate()
                    } else {
                        //进入搜索状态
                        state = State.SEARCHING
                        Log.d("xcl_debug", "onAnimationEnd: 开始搜索中动画---")
                        searchingAnim.start()
                    }
                }

                override fun onAnimationCancel(animation: Animator) {}

                override fun onAnimationRepeat(animation: Animator) {}

            })
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        canvas.let {
            it.save()
            it.translate(width / 2f, height / 2f)
            drawByState(it)
            it.restore()
        }
    }

    private fun drawByState(it: Canvas) {
        when (state) {
            State.IDLE -> {
                it.drawPath(searchPath, paint)
            }

            State.PREPARE, State.FINISHED -> {
                searchPathDst.reset()
                searchPathMeasure.getSegment(
                    (searchCurProcess / 100f) * searchPathMeasure.length,
                    searchPathMeasure.length,
                    searchPathDst,
                    true
                )
                it.drawPath(searchPathDst, paint)
            }

            State.SEARCHING -> {
                circlePathDst.reset()
                val circleRange = if (circleCurProcess > 50f) {
                    (100f - circleCurProcess) * 0.02f * circleMaxRange / 2
                } else {
                    circleCurProcess * 0.02f * circleMaxRange / 2
                }
                Log.d("xcl_debug", "drawByState: circleRange = $circleRange")
                circlePathMeasure.getSegment(
                    max(0f, (circleCurProcess / 100f * circlePathMeasure.length) - circleRange),
                    min(
                        circlePathMeasure.length,
                        (circleCurProcess / 100f * circlePathMeasure.length) + circleRange
                    ), circlePathDst, true
                )
                it.drawPath(circlePathDst, paint)
            }
        }
    }

    fun setStateAndProgressForTest(state: Int, curProgress: Float) {
        this.state = when (state) {
            0 -> State.IDLE
            1 -> State.PREPARE
            2 -> State.SEARCHING
            else -> State.FINISHED
        }
        if (this.state == State.PREPARE) {
            searchCurProcess = curProgress
        } else if (this.state == State.SEARCHING) {
            circleCurProcess = curProgress
        } else if (this.state == State.FINISHED) {
            searchCurProcess = 100f - curProgress
        }
        invalidate()
    }

    /**
     * 初始状态	初始状态，没有任何动效，只显示一个搜索标志 🔍
     * 准备搜索	放大镜图标逐渐变化为一个点
     * 正在搜索	围绕这一个圆环运动，并且线段长度会周期性变化
     * 准备结束	从一个点逐渐变化成为放大镜图标
     */
    private enum class State {
        IDLE, PREPARE, SEARCHING, FINISHED
    }

}