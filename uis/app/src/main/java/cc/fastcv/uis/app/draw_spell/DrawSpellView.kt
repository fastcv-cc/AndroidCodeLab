package cc.fastcv.uis.app.draw_spell

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import androidx.core.graphics.drawable.toBitmap
import cc.fastcv.uis.app.R

class DrawSpellView : View {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes)


    private val mPath = Path()

    private val mGradientStartColor = Color.parseColor("#0000FF")
    private val mGradientEndColor = Color.parseColor("#FF0000")

    private var mTile = Shader.TileMode.CLAMP

    private lateinit var mOtherShader: Shader

    fun reset() {
        mPath.reset()
        invalidate()
    }

    private val mGradientRectF = RectF()

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mGradientRectF.set(
            w / 2.0f - 300.0f,
            h / 2.0f - 400.0f,
            w / 2.0f + 300.0f,
            h / 2.0f + 400.0f
        )
        mPaint.shader = LinearGradient(
            mGradientRectF.left,
            mGradientRectF.top,
            mGradientRectF.right,
            mGradientRectF.bottom,
            mGradientStartColor,
            mGradientEndColor,
            mTile
        )

        mOtherShader = LinearGradient(
            mGradientRectF.left,
            mGradientRectF.top,
            mGradientRectF.right,
            mGradientRectF.bottom,
            mGradientStartColor,
            mGradientEndColor,
            mTile
        )
    }

    private var spell = buildBitmap(16.0f)

    private var shaderType = 0

    private val mPaint = Paint().apply {
        color = Color.BLACK
        isAntiAlias = true
        style = Paint.Style.STROKE
        strokeWidth = 35.0f
        strokeCap = Paint.Cap.ROUND
        strokeJoin = Paint.Join.ROUND
    }

    fun setTileMode(type: Int) {
        when (type) {
            0 -> mTile = Shader.TileMode.CLAMP
            1 -> mTile = Shader.TileMode.MIRROR
            2 -> mTile = Shader.TileMode.REPEAT
        }
        buildPaintShader()
    }

    fun setShader(type: Int) {
        shaderType = type
        buildPaintShader()
    }

    private fun buildPaintShader() {
        when (shaderType) {
            0 -> mPaint.shader = LinearGradient(
                mGradientRectF.left,
                mGradientRectF.top,
                mGradientRectF.right,
                mGradientRectF.bottom,
                mGradientStartColor,
                mGradientEndColor,
                mTile
            )
            1 -> mPaint.shader = RadialGradient(
                mGradientRectF.centerX(),
                mGradientRectF.centerY(),
                mGradientRectF.width() / 2.0f,
                mGradientStartColor,
                mGradientEndColor,
                mTile
            )
            2 -> mPaint.shader = SweepGradient(
                mGradientRectF.centerX(),
                mGradientRectF.centerY(),
                mGradientStartColor,
                mGradientEndColor
            )
            3 -> mPaint.shader = BitmapShader(
                spell,
                mTile,
                mTile
            )
            4 -> mPaint.shader = ComposeShader(
                mOtherShader, BitmapShader(
                    spell,
                    mTile,
                    mTile
                ), PorterDuff.Mode.DST_OUT
            )
        }
    }

    fun setOtherShader(type: Int) {
        when (type) {
            0 -> mOtherShader = LinearGradient(
                mGradientRectF.left,
                mGradientRectF.top,
                mGradientRectF.right,
                mGradientRectF.bottom,
                mGradientStartColor,
                mGradientEndColor,
                mTile
            )
            1 -> mOtherShader = RadialGradient(
                mGradientRectF.centerX(),
                mGradientRectF.centerY(),
                mGradientRectF.width() / 2.0f,
                mGradientStartColor,
                mGradientEndColor,
                mTile
            )
            2 -> mOtherShader =
                SweepGradient(
                    mGradientRectF.centerX(),
                    mGradientRectF.centerY(),
                    Color.parseColor("#33ad3f"),
                    Color.parseColor("#2200ff")
                )
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                val x = event.x
                val y = event.y
                mPath.moveTo(x, y)
                invalidate()
            }
            MotionEvent.ACTION_MOVE -> {
                val x = event.x
                val y = event.y
                mPath.lineTo(x, y)
                invalidate()
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
            }
            else -> {}
        }
        return true
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawPath(mPath, mPaint)
    }

    fun setBitmapSize(size: Float) {
        spell = buildBitmap(size)
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun buildBitmap(size: Float): Bitmap {
        return resources.getDrawable(R.drawable.ic_spell, null).toBitmap(
            TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                size,
                resources.displayMetrics
            ).toInt(), TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                size,
                resources.displayMetrics
            ).toInt()
        )
    }


}