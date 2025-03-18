package cc.fastcv.libs.app.local_data_store.proto_data_store

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import androidx.datastore.preferences.protobuf.InvalidProtocolBufferException
import cc.fastcv.libs.app.proto.ProtoObj
import java.io.InputStream
import java.io.OutputStream

object ProtoObjSerializer: Serializer<ProtoObj> {
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