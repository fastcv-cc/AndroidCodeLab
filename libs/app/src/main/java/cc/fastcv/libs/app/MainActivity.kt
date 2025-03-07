package cc.fastcv.libs.app

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import cc.fastcv.libs.app.local_data_store.LocalDataStoreActivity
import cc.fastcv.libs.app.logger.LoggerActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val rvItem = findViewById<RecyclerView>(R.id.rvItem)
        rvItem.adapter = ItemAdapter(buildLibItemList())
    }

    private fun buildLibItemList(): List<LibItem> {
        return listOf(
            LibItem("Logger库", "一个日志打印库", Intent(this, LoggerActivity::class.java)),
            LibItem("LocalDataStore库", "本地轻量化数据持久化库", Intent(this, LocalDataStoreActivity::class.java)),
        )
    }
}

private data class LibItem(val title: String, val description: String, val intent: Intent)

private class ItemAdapter(val items: List<LibItem>) : RecyclerView.Adapter<ItemViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.layout_item_lib, parent, false)
        return ItemViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.tvTitle.text = items[position].title
        holder.tvDescription.text = items[position].description
        holder.itemView.setOnClickListener {
            it.context.startActivity(items[position].intent)
        }
    }

}

private class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
    val tvDescription: TextView = itemView.findViewById(R.id.tvDescription)

}