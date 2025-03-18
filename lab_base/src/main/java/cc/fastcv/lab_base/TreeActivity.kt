package cc.fastcv.lab_base

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cc.fastcv.basic_general.activity.BaseActivity
import cc.fastcv.recyclerview_ext.BaseRecyclerViewAdapter
import cc.fastcv.recyclerview_ext.BaseViewHolder
import cc.fastcv.recyclerview_ext.OnItemClickListener

abstract class TreeActivity : BaseActivity(), OnItemClickListener<LibItem> {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tree)

        val rvItem = findViewById<RecyclerView>(R.id.rvItem)
        val adapter = ItemAdapter(buildLibItemList())
        rvItem.adapter = adapter
        adapter.setOnItemClickListener(this)
    }

    abstract fun buildLibItemList(): List<LibItem>

    override fun onItemClick(view: View?, position: Int, t: LibItem) {
        startActivity(t.intent)
    }

}

data class LibItem(val title: String, val description: String, val intent: Intent)

private class ItemAdapter(val items: List<LibItem>) : BaseRecyclerViewAdapter<LibItem>() {
    override fun getLayoutId(viewType: Int): Int {
        return R.layout.layout_item_lib
    }

    override fun getDataByPosition(position: Int): LibItem {
        return items[position]
    }

    override fun getTotalSize(): Int {
        return items.size
    }

    override fun convert(holder: BaseViewHolder, data: LibItem, position: Int) {
        holder.getView<TextView>(R.id.tvTitle).text = items[position].title
        holder.getView<TextView>(R.id.tvDescription).text = items[position].description
    }
}