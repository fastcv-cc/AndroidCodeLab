package cc.fastcv.uis.app.corner_frame_layout

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.Log
import android.widget.FrameLayout

/**
 * 综合下来 只能是用Bitmap当成Canvas 让原本的canvas的内容画在上面 然后把它当成目标 进行 Xfermode
 */
class CornerFrameLayout1 : FrameLayout {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    private val mRoundRect = RectF(0f, 0f, 0f, 0f)

    private val mPath = Path()

    private var mBackground: Drawable? = null

    private var mViewWidth = 0
    private var mViewHeight = 0

    private val mPaint = Paint().apply {
        isAntiAlias = true
        style = Paint.Style.FILL
        xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        setWillNotDraw(false)
        mViewWidth = w
        mViewHeight = h
        mRoundRect.right = w * 1.0f
        mRoundRect.bottom = h * 1.0f
        //Path 的填充模式为反奇偶规则
        mPath.fillType = Path.FillType.INVERSE_EVEN_ODD
        mPath.addRoundRect(
            mRoundRect,
            floatArrayOf(100f, 100f, 100f, 100f, 100f, 100f, 100f, 100f), Path.Direction.CW
        )
    }

    override fun draw(canvas: Canvas) {
        Log.d("xcl_debug", "----------------------------")
        canvas.let {
            val saveLayer = it.saveLayer(null, null)
            Log.d("xcl_debug", "draw: mBackground = $mBackground")
            mBackground?.bounds = Rect(0, 0, mViewWidth, mViewHeight)
            mBackground?.draw(it)
            it.drawPath(mPath, mPaint)
            it.restoreToCount(saveLayer)
        }
        super.draw(canvas)
    }

    override fun setBackground(background: Drawable?) {
        mBackground = background
    }
}