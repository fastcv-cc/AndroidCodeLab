package cc.fastcv.sql

import android.content.Intent
import cc.fastcv.lab_base.LibItem
import cc.fastcv.lab_base.TreeActivity
import cc.fastcv.sql.sqlite.SqliteActivity

class SqlMainActivity : TreeActivity() {
    override fun buildLibItemList(): List<LibItem> {
        return listOf(
            LibItem(
                "Sqlite原生用法",
                "Sqlite原生用法",
                Intent(this, SqliteActivity::class.java)
            )
        )
    }
}