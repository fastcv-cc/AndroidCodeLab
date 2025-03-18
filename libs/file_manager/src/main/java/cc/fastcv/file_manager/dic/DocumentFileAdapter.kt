package cc.fastcv.file_manager.dic

import android.annotation.SuppressLint
import android.widget.ImageView
import android.widget.TextView
import androidx.documentfile.provider.DocumentFile
import cc.fastcv.file_manager.R
import cc.fastcv.recyclerview_ext.BaseRecyclerViewAdapter
import cc.fastcv.recyclerview_ext.BaseViewHolder

internal class DocumentFileAdapter : BaseRecyclerViewAdapter<DocumentFile>() {

    private val files = mutableListOf<DocumentFile>()

    @SuppressLint("NotifyDataSetChanged")
    fun setData(files: List<DocumentFile>) {
        this.files.clear()
        this.files.addAll(files)
        notifyDataSetChanged()
    }

    override fun getLayoutId(viewType: Int): Int {
        return R.layout.item_file
    }

    override fun getDataByPosition(position: Int): DocumentFile {
        return files[position]
    }

    override fun getTotalSize(): Int {
        return files.size
    }

    override fun convert(holder: BaseViewHolder, data: DocumentFile, position: Int) {
        if (data.isDirectory) {
            holder.getView<ImageView>(R.id.iv_icon).setImageResource(R.drawable.ic_folder)
        } else {
            holder.getView<ImageView>(R.id.iv_icon).setImageResource(R.drawable.ic_file)
        }

        holder.getView<TextView>(R.id.tv_title).text = data.name
        holder.getView<ImageView>(R.id.iv_state).setImageResource(getStateResource(position))
    }

    private fun getStateResource(position: Int): Int {
        val read: Boolean = files[position].canRead()
        val write: Boolean = files[position].canRead()
        return if (read && !write) {
            R.drawable.ic_only_read
        } else if (!read && write) {
            R.drawable.ic_only_write
        } else {
            R.drawable.ic_read_and_write
        }
    }
}