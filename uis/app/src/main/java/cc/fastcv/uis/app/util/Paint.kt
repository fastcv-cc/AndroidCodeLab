package cc.fastcv.uis.app.util

import android.graphics.Paint

fun Paint.getTextYOffset(): Float {
    return -fontMetrics.top - (fontMetrics.bottom - fontMetrics.top) / 2.0f
}

fun Paint.getTextHeight(): Float {
    return fontMetrics.bottom - fontMetrics.top
}

fun Paint.getTextWidth(text: String): Float {
    return measureText(text)
}