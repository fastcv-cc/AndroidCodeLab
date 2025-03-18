package cc.fastcv.libs.app.file_manager.internal_storage

import android.os.Bundle
import android.widget.Button
import cc.fastcv.file_manager.FileManager
import cc.fastcv.lab_base.LeafActivity
import cc.fastcv.libs.app.R

class InternalStorageActivity : LeafActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_internal_storage)

        findViewById<Button>(R.id.bt1).setOnClickListener {
            filesDir.getParent()?.let { FileManager.openDicByPath(this, it) }
        }
    }

}