package cc.fastcv.recyclerview_ext

import android.view.View

interface OnItemLongClickListener<S> {
    fun onItemLongClick(view: View?, position: Int, t: S)
}