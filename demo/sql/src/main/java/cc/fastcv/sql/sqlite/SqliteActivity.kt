package cc.fastcv.sql.sqlite

import android.os.Bundle
import android.widget.Button
import cc.fastcv.lab_base.LeafActivity
import cc.fastcv.sql.R
import cc.fastcv.sql.sqlite.db.Database

class SqliteActivity : LeafActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sqlite)
        Database.init(applicationContext)
        findViewById<Button>(R.id.bt1).setOnClickListener {

        }

        findViewById<Button>(R.id.bt2).setOnClickListener {

        }

        findViewById<Button>(R.id.bt3).setOnClickListener {

        }
    }

}