package cc.fastcv.date_switch_view

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.util.TypedValue
import cc.fastcv.date_switch_view.DateSwitchView

class DateSwitchViewParams {

    /**
     * 自定义tab宽度
     */
    internal var customizeTabWidth: Int = -1

    /**
     * 自定义tab宽度
     */
    internal var customizeTabHeight: Int = -1

    /**
     * 自定义tab的外边距
     */
    internal var customizeTabMargin: Int = -1

    /**
     * 自定义的tab文字颜色
     */
    internal var customizeTabTextColor: Int = -1

    /**
     * 自定义的tab选中文字颜色
     */
    internal var customizeTabSelectedTextColor: Int = -1

    /**
     * tab 日文本
     */
    internal var dayTabText: String = ""

    /**
     * tab 月文本
     */
    internal var monthTabText: String = ""

    /**
     * tab文本字体大小
     */
    internal var tabTextSize: Float = 0f

    /**
     * 当前选中的文本下标
     */
    internal var selectedIndex = 0

    /**
     * 周的词条
     */
    internal var weekStrArrays = mutableListOf<String>()

    /**
     * 月的词条
     */
    internal var monthStrArrays = mutableListOf<String>()

    /**
     * 动画时长
     */
    internal var animDuration = 250L

    /**
     * 每周开始
     */
    internal var weekStart = DateSwitchView.WEEK_START_WITH_MONDAY

    /**
     * 周文本/日期文本间距
     */
    internal var weekTextMargin = 0

    fun initWithAttributeSet(context:Context,attrs: AttributeSet) {
        val a = context.obtainStyledAttributes(attrs, R.styleable.DateSwitchView)
        customizeTabWidth =
            a.getDimensionPixelSize(R.styleable.DateSwitchView_tabWidth, dp2px(context, 158))
        customizeTabHeight =
            a.getDimensionPixelSize(R.styleable.DateSwitchView_tabHeight, dp2px(context, 36))
        customizeTabMargin =
            a.getDimensionPixelSize(R.styleable.DateSwitchView_tabMargin, dp2px(context, 4))
        customizeTabTextColor =
            a.getColor(
                R.styleable.DateSwitchView_tabTextColor,
                Color.parseColor("#61003324")
            )
        customizeTabSelectedTextColor =
            a.getColor(
                R.styleable.DateSwitchView_tabSelectedTextColor,
                Color.parseColor("#F9F9F9")
            )
        dayTabText = a.getString(R.styleable.DateSwitchView_dayTabText) ?: ""
        monthTabText = a.getString(R.styleable.DateSwitchView_monthTabText) ?: ""
        tabTextSize =
            a.getDimension(R.styleable.DateSwitchView_tabTextSize, -1f)
        tabTextSize = if (tabTextSize < 0) {
            12.0f
        } else {
            px2sp(context, tabTextSize)
        }
        val weekStrArraysId = a.getResourceId(R.styleable.DateSwitchView_weekStrArrays, -1)
        if (weekStrArraysId == -1) {
            throw IllegalArgumentException("must be set weekStrArrays!!!")
        }
        for (weekStr in context.resources.getStringArray(weekStrArraysId)) {
            weekStrArrays.add(weekStr)
        }
        if (weekStrArrays.size != 7) {
            throw IllegalArgumentException("weekStrArrays size must be 7!!!")
        }

        val monthStrArraysId = a.getResourceId(R.styleable.DateSwitchView_monthStrArrays, -1)
        if (monthStrArraysId == -1) {
            throw IllegalArgumentException("must be set monthStrArrays!!!")
        }
        for (monthStr in context.resources.getStringArray(monthStrArraysId)) {
            monthStrArrays.add(monthStr)
        }
        if (monthStrArrays.size != 12) {
            throw IllegalArgumentException("monthStrArrays size must be 12!!!")
        }

        weekStart = a.getInt(
            R.styleable.DateSwitchView_weekStartWith,
            DateSwitchView.WEEK_START_WITH_MONDAY
        )
        weekTextMargin = a.getDimensionPixelSize(
            R.styleable.DateSwitchView_weekTextMargin,
            dp2px(context, 5)
        )
        a.recycle()
    }

    private fun px2sp(context: Context, dpValue: Float): Float {
        val density = context.resources.displayMetrics.scaledDensity
        return dpValue / density
    }

    private fun dp2px(context: Context, dpValue: Int): Int {
        return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_SP,
            dpValue.toFloat(),
            context.resources.displayMetrics
        ).toInt()
    }

}