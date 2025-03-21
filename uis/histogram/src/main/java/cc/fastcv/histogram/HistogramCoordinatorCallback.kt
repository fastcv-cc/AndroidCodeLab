package cc.fastcv.histogram

interface HistogramCoordinatorCallback {
    fun onHistogramSelect(position:Int, info: HistogramInfo)
    fun onHistogramCalc()
}