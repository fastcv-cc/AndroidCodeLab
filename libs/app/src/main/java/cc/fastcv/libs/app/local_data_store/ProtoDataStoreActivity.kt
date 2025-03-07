package cc.fastcv.libs.app.local_data_store

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.CorruptionException
import androidx.datastore.core.DataStore
import androidx.datastore.core.Serializer
import androidx.datastore.dataStore
import androidx.datastore.preferences.protobuf.InvalidProtocolBufferException
import androidx.lifecycle.lifecycleScope
import cc.fastcv.libs.app.R
import cc.fastcv.libs.app.proto.Car
import cc.fastcv.libs.app.proto.Children
import cc.fastcv.libs.app.proto.ProtoObj
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.io.InputStream
import java.io.OutputStream

private const val TAG = "ProtoDataStore"

class ProtoDataStoreActivity : AppCompatActivity() {

    private val Context.protoObjStore: DataStore<ProtoObj> by dataStore(
        fileName = "protoObjStore.pb",
        serializer = ProtoObjSerializer
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_proto_data_store)

        findViewById<Button>(R.id.bt1).setOnClickListener {
            lifecycleScope.launch {
                protoObjStore.updateData {
                    it.toBuilder()
                        .setName("张三")
                        .setAge(18)
                        .setAddress("广东省深圳市")
                        .setWeight(65.3f)
                        .setMoney(2025.987)
                        .setBirthday(System.currentTimeMillis())
                        .setIsMan(true)
                        .addCars(Car.newBuilder().setName("奔驰").setPrice(200000))
                        .addCars(Car.newBuilder().setName("宝马").setPrice(180000))
                        .addChildrens(Children.newBuilder().setName("张老一").setAge(10))
                        .addChildrens(Children.newBuilder().setName("张老二").setAge(7))
                        .build()
                }
            }
        }

        findViewById<Button>(R.id.bt2).setOnClickListener {
            lifecycleScope.launch {
                val protoObj = protoObjStore.data.first()
                if (ProtoObj.getDefaultInstance() == protoObj) {
                    Log.d(TAG, "protoObj = null")
                } else {
                    Log.d(TAG, "protoObj = $protoObj")
                }
            }
        }

        findViewById<Button>(R.id.bt3).setOnClickListener {
            lifecycleScope.launch {
                protoObjStore.data.collectLatest {
                    Log.d(TAG, "值变化 protoObj = $it")
                }
            }
        }

        findViewById<Button>(R.id.bt4).setOnClickListener {
            Toast.makeText(this,"不支持删除",Toast.LENGTH_SHORT).show()
        }
    }

}

private object ProtoObjSerializer: Serializer<ProtoObj> {
    override val defaultValue: ProtoObj = ProtoObj.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): ProtoObj {
        try {
            return ProtoObj.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", exception)
        }
    }

    override suspend fun writeTo(t: ProtoObj, output: OutputStream) {
        t.writeTo(output)
    }
}