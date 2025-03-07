package cc.fastcv.libs.app.local_data_store

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import androidx.datastore.preferences.protobuf.InvalidProtocolBufferException
import cc.fastcv.libs.app.proto.MultiProtoObj
import java.io.InputStream
import java.io.OutputStream

object MultiProtoObjSerializer: Serializer<MultiProtoObj> {
    override val defaultValue: MultiProtoObj = MultiProtoObj.getDefaultInstance()

    override suspend fun readFrom(input: InputStream): MultiProtoObj {
        try {
            return MultiProtoObj.parseFrom(input)
        } catch (exception: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", exception)
        }
    }

    override suspend fun writeTo(t: MultiProtoObj, output: OutputStream) {
        t.writeTo(output)
    }

}