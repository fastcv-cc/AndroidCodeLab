package cc.fastcv.libs.app.local_data_store

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.core.MultiProcessDataStoreFactory
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.lifecycle.lifecycleScope
import cc.fastcv.libs.app.OtherProcessService
import cc.fastcv.libs.app.R
import cc.fastcv.libs.app.proto.MultiProtoObj
import cc.fastcv.local_data_store.LocalDataStore
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File

private const val TAG = "LocalDataStoreActivity"

class LocalDataStoreActivity : AppCompatActivity() {

    private val dataStore: DataStore<MultiProtoObj> = MultiProcessDataStoreFactory.create(
        serializer = MultiProtoObjSerializer,
        produceFile = {
            File("${cacheDir.path}/multi_proto_obj.preferences_pb")
        },
        corruptionHandler = ReplaceFileCorruptionHandler { MultiProtoObj.getDefaultInstance() }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_local_data_store)

        findViewById<Button>(R.id.bt1).setOnClickListener {
            startActivity(Intent(this, SharedPreferencesActivity::class.java))
        }

        findViewById<Button>(R.id.bt2).setOnClickListener {
            startActivity(Intent(this, PreferencesDataStoreActivity::class.java))
        }

        findViewById<Button>(R.id.bt3).setOnClickListener {
            startActivity(Intent(this, ProtoDataStoreActivity::class.java))
        }

        findViewById<Button>(R.id.bt4).setOnClickListener {
            lifecycleScope.launch {
                Log.d(TAG, "启动OtherProcessService服务")
                startService(Intent(this@LocalDataStoreActivity, OtherProcessService::class.java))
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

        findViewById<Button>(R.id.bt5).setOnClickListener {
            testLocalDataStoreFunction()
        }

    }

    private fun testLocalDataStoreFunction() {

        lifecycleScope.launch {
            val storeName = "localDataStore"
            val stringKey = "stringKey"
            LocalDataStore.saveString(
                this@LocalDataStoreActivity,
                storeName,
                stringKey,
                "我是一个值"
            )
            val stringValue = LocalDataStore.getString(
                this@LocalDataStoreActivity,
                storeName,
                stringKey,
                "我是一个默认值"
            )
            Log.d(TAG, "testLocalDataStoreFunction: stringValue = $stringValue")
            val objKey = "objKey"
            LocalDataStore.saveObj(this@LocalDataStoreActivity,storeName,objKey,LocalDataStoreEntity("Hello",20))
            val objValue = LocalDataStore.getObj(
                this@LocalDataStoreActivity,
                storeName,
                objKey,LocalDataStoreEntity::class.java
            )
            Log.d(TAG, "testLocalDataStoreFunction: objValue = $objValue")
        }

    }

}

private data class LocalDataStoreEntity(val name: String, val age: Int)