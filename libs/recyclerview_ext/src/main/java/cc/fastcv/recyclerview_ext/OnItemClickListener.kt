package cc.fastcv.recyclerview_ext

import android.view.View

interface OnItemClickListener<T> {
    fun onItemClick(view: View?, position: Int, t: T)
}