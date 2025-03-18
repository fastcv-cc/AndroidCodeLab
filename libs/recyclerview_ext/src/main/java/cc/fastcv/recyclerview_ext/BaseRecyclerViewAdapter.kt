package cc.fastcv.recyclerview_ext

import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

/**
 * 基础适配器封装
 */
abstract class BaseRecyclerViewAdapter<T> : RecyclerView.Adapter<BaseViewHolder>() {

    //item点击事件
    private var onItemClickListener: OnItemClickListener<T>? = null
    //item长按事件
    private var onItemLongClickListener: OnItemLongClickListener<T>? = null

    //获取item布局文件id
    abstract fun getLayoutId(viewType: Int): Int

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return BaseViewHolder.getViewHolder(parent, getLayoutId(viewType))
    }

    protected abstract fun getDataByPosition(position: Int): T

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        convert(holder, getDataByPosition(position), holder.adapterPosition)
        getOnClickItemView(holder).setOnClickListener {
            if (onItemClickListener != null) {
                val pos = holder.layoutPosition
                onItemClickListener!!.onItemClick(
                    holder.itemView,
                    pos,
                    getDataByPosition(holder.adapterPosition)
                )
            }
        }
        getOnClickItemView(holder).setOnLongClickListener {
            if (onItemLongClickListener != null) {
                val pos = holder.layoutPosition
                onItemLongClickListener!!.onItemLongClick(
                    holder.itemView,
                    pos,
                    getDataByPosition(holder.adapterPosition)
                )
            }
            true
        }
    }

    //获取点击/长按事件的itemView 默认是holder的itemView
    open fun getOnClickItemView(holder: BaseViewHolder): View {
        return holder.itemView
    }

    //获取数据源数量
    override fun getItemCount(): Int {
        return getTotalSize()
    }

    abstract fun getTotalSize(): Int

    /**
     * 设置Item的点击事件
     */
    open fun setOnItemClickListener(listener: OnItemClickListener<T>?) {
        onItemClickListener = listener
    }

    /**
     * 设置Item的长按事件
     */
    open fun setOnItemLongClickListener(onItemLongClickListener: OnItemLongClickListener<T>?) {
        this.onItemLongClickListener = onItemLongClickListener
    }

    abstract fun convert(holder: BaseViewHolder, data: T, position: Int)
}