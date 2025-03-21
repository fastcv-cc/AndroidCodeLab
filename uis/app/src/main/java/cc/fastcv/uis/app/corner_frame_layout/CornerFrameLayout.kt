package cc.fastcv.uis.app.corner_frame_layout

import android.content.Context
import android.graphics.Canvas
import android.graphics.Path
import android.graphics.RectF
import android.util.AttributeSet
import android.widget.FrameLayout

class CornerFrameLayout : FrameLayout {

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes)

    private val mRoundRect = RectF(0f, 0f, 0f, 0f)

    private val mPath = Path()

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mRoundRect.right = w * 1.0f
        mRoundRect.bottom = h * 1.0f
        mPath.addRoundRect(
            mRoundRect,
            floatArrayOf(100f, 100f, 100f, 100f, 100f, 100f, 100f, 100f), Path.Direction.CW
        )
    }

    override fun draw(canvas: Canvas) {
        canvas.clipPath(mPath)
        super.draw(canvas)
    }
}