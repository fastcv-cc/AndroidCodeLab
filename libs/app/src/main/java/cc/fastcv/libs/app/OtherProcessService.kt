package cc.fastcv.libs.app

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.core.MultiProcessDataStoreFactory
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.lifecycleScope
import cc.fastcv.libs.app.local_data_store.MultiProtoObjSerializer
import cc.fastcv.libs.app.proto.MultiProtoObj
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.io.File

private const val TAG = "OtherProcessService"
class OtherProcessService : LifecycleService() {

    private val dataStore: DataStore<MultiProtoObj> = MultiProcessDataStoreFactory.create(
        serializer = MultiProtoObjSerializer,
        produceFile = {
            File("${cacheDir.path}/multi_proto_obj.preferences_pb")
        },
        corruptionHandler = ReplaceFileCorruptionHandler { MultiProtoObj.getDefaultInstance() }
    )

    override fun onCreate() {
        super.onCreate()
        Log.d(TAG, "OtherProcessService onCreate: ")
        lifecycleScope.launch {
            dataStore.data.collectLatest {
                Log.d(TAG, "onCreate: OtherProcessService监听到值变化：$it")
            }
        }
    }
}

