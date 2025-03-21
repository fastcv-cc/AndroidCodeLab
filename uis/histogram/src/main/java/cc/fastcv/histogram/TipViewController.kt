package cc.fastcv.histogram

import android.annotation.SuppressLint
import android.content.Context
import android.util.TypedValue
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import kotlin.math.max

class TipViewController(private val extView: HistogramExtView) {

    @SuppressLint("ClickableViewAccessibility")
    private var llTip: LinearLayout = extView.findViewById(R.id.ll_tip)
    private var tvTop: TextView = extView.findViewById(R.id.tv_top)
    private var tvBottom: TextView = extView.findViewById(R.id.tv_bottom)
    private var llTipWidth = dp2px(extView.context, 96)

    private var lastIndex = -1

    fun moveTo(index: Int) {
        lastIndex = index

        if (index == -1) {
            lastIndex = -1
            llTip.visibility = View.GONE
            return
        }

        if (llTip.visibility == View.GONE) {
            llTip.visibility = View.VISIBLE
        }

        val maxMargin = extView.width * 1f - llTipWidth / 2f

        val marginStart = if (extView.getRelativeX(index) > maxMargin) {
            //右边不够时
            maxMargin - llTipWidth / 2f
        } else {
            max(extView.getRelativeX(index) - llTipWidth / 2f, 0f)
        }

        val layoutParams = llTip.layoutParams as FrameLayout.LayoutParams
        layoutParams.marginStart = marginStart.toInt()
        llTip.layoutParams = layoutParams
        showTip()
    }

    private fun dp2px(context: Context, value: Int): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP,
            value.toFloat(),
            context.resources.displayMetrics
        ).toInt()
    }

    fun rePosition() {
        if (lastIndex == -1) {
            return
        }
        moveTo(lastIndex)
    }

    private fun showTip() {
        if (lastIndex == -1) {
            return
        }

        val info = extView.getDataInfoByIndex(lastIndex)
        info?.let {
            val adapter = extView.getTipAdapter()
            tvTop.text = adapter.getTopText(lastIndex, it)
            tvBottom.text = adapter.getBottomText(lastIndex, it)
        }
    }
}