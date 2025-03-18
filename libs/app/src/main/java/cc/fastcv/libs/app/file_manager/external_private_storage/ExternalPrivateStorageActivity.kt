package cc.fastcv.libs.app.file_manager.external_private_storage

import android.os.Bundle
import android.widget.Button
import cc.fastcv.file_manager.FileManager
import cc.fastcv.lab_base.LeafActivity
import cc.fastcv.libs.app.R

class ExternalPrivateStorageActivity : LeafActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_external_private_storage)
        findViewById<Button>(R.id.bt1).setOnClickListener {
            getExternalFilesDir("")!!.getParent()?.let {
                FileManager.openDicByPath(
                    this,
                    it
                )
            }
        }
    }

}