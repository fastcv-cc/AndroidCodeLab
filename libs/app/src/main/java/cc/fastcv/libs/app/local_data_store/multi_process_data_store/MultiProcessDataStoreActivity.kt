package cc.fastcv.libs.app.local_data_store.multi_process_data_store

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.datastore.core.DataStore
import androidx.datastore.core.MultiProcessDataStoreFactory
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.lifecycle.lifecycleScope
import cc.fastcv.lab_base.LeafActivity
import cc.fastcv.libs.app.R
import cc.fastcv.libs.app.proto.MultiProtoObj
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File

class MultiProcessDataStoreActivity : LeafActivity() {

    companion object {
        private const val TAG = "MultiProcessDataStore"
    }

    private val dataStore: DataStore<MultiProtoObj> = MultiProcessDataStoreFactory.create(
        serializer = MultiProtoObjSerializer,
        produceFile = {
            File("${cacheDir.path}/multi_proto_obj.preferences_pb")
        },
        corruptionHandler = ReplaceFileCorruptionHandler { MultiProtoObj.getDefaultInstance() }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_multi_process_data_store)

        findViewById<Button>(R.id.bt1).setOnClickListener {
            lifecycleScope.launch {
                Log.d(TAG, "启动OtherProcessService服务")
                startService(Intent(this@MultiProcessDataStoreActivity, OtherProcessService::class.java))
                Log.d(TAG, "3秒后修改MultiProtoObj DataStore的值")
                delay(3000)
                dataStore.updateData {
                    it.toBuilder()
                        .setName("王五")
                        .setAge((System.currentTimeMillis() / 10000).toInt())
                        .build()
                }
            }
        }

    }
}