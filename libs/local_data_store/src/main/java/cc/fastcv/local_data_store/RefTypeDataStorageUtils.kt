package cc.fastcv.local_data_store

import android.content.Context
import android.text.TextUtils
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class RefTypeDataStorageUtils {

    /**
     * json格式数据转换器
     */
    private val gson = Gson()

    private fun <T> fromJson(jsonString: String, cls: Class<T>): T? {
        return gson.fromJson(jsonString, cls)
    }

    private fun <T> fromJson(jsonString: String, type: TypeToken<T>): T {
        val resultType = type.type
        return gson.fromJson<T>(jsonString, resultType)
    }

    private fun toJson(any: Any): String {
        return gson.toJson(any)
    }

    suspend fun saveObj(context: Context, storeName: String, key: String, value: Any) {
        val dataStore = preferencesDataStore(context = context, name = storeName)
        val realValue = toJson(value)
        dataStore.edit { it[stringPreferencesKey(key)] = realValue }
    }

    suspend fun <T> getObj(
        context: Context,
        storeName: String,
        key: String,
        type: TypeToken<T>
    ): T? {
        val dataStore = preferencesDataStore(context = context, name = storeName)
        val default = null
        val jsonStr = dataStore.data.map { it[stringPreferencesKey(key)] ?: default }.first()
        if (TextUtils.isEmpty(jsonStr)) {
            return null
        }
        return fromJson(jsonStr!!, type)
    }

    suspend fun <T> getObj(
        context: Context,
        storeName: String,
        key: String,
        cls: Class<T>
    ): T? {
        val dataStore = preferencesDataStore(context = context, name = storeName)
        val default = null
        val jsonStr = dataStore.data.map { it[stringPreferencesKey(key)] ?: default }.first()
        if (TextUtils.isEmpty(jsonStr)) {
            return null
        }
        return fromJson(jsonStr!!, cls)
    }

    suspend fun <T> removeDataByObjKey(context: Context, storeName: String, key: String) {
        val dataStore = preferencesDataStore(context = context, name = storeName)
        dataStore.edit { it.remove(stringPreferencesKey(key)) }
    }
}