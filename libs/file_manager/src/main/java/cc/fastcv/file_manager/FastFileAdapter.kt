package cc.fastcv.file_manager

import android.annotation.SuppressLint
import android.widget.ImageView
import cc.fastcv.recyclerview_ext.BaseRecyclerViewAdapter
import cc.fastcv.recyclerview_ext.BaseViewHolder

internal class FastFileAdapter : BaseRecyclerViewAdapter<FastFile>() {

    private val files = mutableListOf<FastFile>()

    @SuppressLint("NotifyDataSetChanged")
    fun setData(files: List<FastFile>) {
        this.files.clear()
        this.files.addAll(files)
        notifyDataSetChanged()
    }

    override fun getLayoutId(viewType: Int): Int {
        return R.layout.item_fast_file
    }

    override fun getDataByPosition(position: Int): FastFile {
        return files[position]
    }

    override fun getTotalSize(): Int {
        return files.size
    }

    override fun convert(holder: BaseViewHolder, data: FastFile, position: Int) {
        setDrawableId(data, holder.getView(R.id.iv_icon))
    }

    private fun setDrawableId(data: FastFile, imageView: ImageView) {
        val mineType = data.getMineType(imageView.context)
        when (mineType) {
            MimeType._png, MimeType._images, MimeType._jpeg, MimeType._jpg, MimeType._webp, MimeType._m4u, MimeType._m4v -> imageView.setImageURI(
                data.uri
            )

            MimeType._gif -> imageView.setImageResource(R.drawable.ic_file_gif)
            MimeType._bmp -> imageView.setImageResource(R.drawable.ic_file_bmp)
            MimeType._3gp, MimeType._avi, MimeType._mp4, MimeType._asf, MimeType._mov,
            MimeType._mpe, MimeType._mpeg, MimeType._mpg, MimeType._mpg4 -> imageView.setImageBitmap(
                data.getVideoThumbnail(imageView.context)
            )

            MimeType._apk -> imageView.setImageResource(R.drawable.ic_file_apk)
            MimeType._m3u, MimeType._m4a, MimeType._m4b, MimeType._m4p, MimeType._mp2,
            MimeType._mp3, MimeType._mpga, MimeType._ogg, MimeType._rmvb, MimeType._wav,
            MimeType._wma, MimeType._wmv -> imageView.setImageResource(R.drawable.ic_file_audio)

            MimeType._text -> imageView.setImageResource(R.drawable.ic_file_text)
            MimeType._doc, MimeType._docx -> imageView.setImageResource(R.drawable.ic_file_doc)
            MimeType._xls, MimeType._xlsx -> imageView.setImageResource(R.drawable.ic_file_xls)
            MimeType._gtar, MimeType._gz, MimeType._z, MimeType._zip, MimeType._tar, MimeType._tgz -> imageView.setImageResource(
                R.drawable.ic_file_zip
            )

            MimeType._htm -> imageView.setImageResource(R.drawable.ic_file_html)
            MimeType._jar -> imageView.setImageResource(R.drawable.ic_file_jar)
            MimeType._js -> imageView.setImageResource(R.drawable.ic_file_js)
            MimeType._pdf -> imageView.setImageResource(R.drawable.ic_file_pdf)
            MimeType._ppt, MimeType._pptx -> imageView.setImageResource(R.drawable.ic_file_ppt)
            else -> R.drawable.ic_file_unknown
        }
    }

}