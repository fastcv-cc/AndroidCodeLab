package cc.fastcv.histogram

import cc.fastcv.histogram.HistogramInfo

abstract class TipAdapter {
    abstract fun getTopText(position: Int, info: HistogramInfo): CharSequence
    abstract fun getBottomText(position: Int, info: HistogramInfo): CharSequence
}