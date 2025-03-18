package cc.fastcv.basic_general.fragment

interface OnVisibilityListener {

    /**
     * 在前台
     */
    fun onInForeground()

    /**
     * 在后台
     */
    fun onInBackground()

}