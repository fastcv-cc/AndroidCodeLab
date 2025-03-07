package cc.fastcv.local_data_store

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import com.google.gson.reflect.TypeToken


object LocalDataStore {

    private val baseTypeDataStorage = BaseTypeDataStorageUtils()

    private val refTypeDataStorage = RefTypeDataStorageUtils()

    suspend fun saveString(context: Context, storeName: String, key: String, value: String) {
        baseTypeDataStorage.saveString(context, storeName, key, value)
    }

    suspend fun saveFloat(context: Context, storeName: String, key: String, value: Float) {
        baseTypeDataStorage.saveFloat(context, storeName, key, value)
    }

    suspend fun saveBoolean(context: Context, storeName: String, key: String, value: Boolean) {
        baseTypeDataStorage.saveBoolean(context, storeName, key, value)
    }

    suspend fun saveLong(context: Context, storeName: String, key: String, value: Long) {
        baseTypeDataStorage.saveLong(context, storeName, key, value)
    }

    suspend fun saveInt(context: Context, storeName: String, key: String, value: Int) {
        baseTypeDataStorage.saveInt(context, storeName, key, value)
    }

    suspend fun saveStringSet(
        context: Context,
        storeName: String,
        key: String,
        value: Set<String>
    ) {
        baseTypeDataStorage.saveStringSet(context, storeName, key, value)
    }

    suspend fun saveObj(context: Context, storeName: String, key: String, value: Any) {
        refTypeDataStorage.saveObj(context, storeName, key, value)
    }

    suspend fun getString(
        context: Context,
        storeName: String,
        key: String,
        default: String
    ): String {
        return baseTypeDataStorage.getString(context, storeName, key, default)
    }

    suspend fun getFloat(context: Context, storeName: String, key: String, default: Float): Float {
        return baseTypeDataStorage.getFloat(context, storeName, key, default)
    }

    suspend fun getBoolean(
        context: Context,
        storeName: String,
        key: String,
        default: Boolean
    ): Boolean {
        return baseTypeDataStorage.getBoolean(context, storeName, key, default)
    }

    suspend fun getLong(context: Context, storeName: String, key: String, default: Long): Long {
        return baseTypeDataStorage.getLong(context, storeName, key, default)
    }

    suspend fun getInt(context: Context, storeName: String, key: String, default: Int): Int {
        return baseTypeDataStorage.getInt(context, storeName, key, default)
    }

    suspend fun getStringSet(
        context: Context,
        storeName: String,
        key: String,
        default: Set<String>
    ): Set<String> {
        return baseTypeDataStorage.getStringSet(context, storeName, key, default)
    }

    suspend fun <T> getObj(
        context: Context,
        storeName: String,
        key: String,
        type: TypeToken<T>
    ): T? {
        return refTypeDataStorage.getObj(context, storeName, key, type)
    }

    suspend fun <T> getObj(
        context: Context,
        storeName: String,
        key: String,
        cls: Class<T>
    ): T? {
        return refTypeDataStorage.getObj(context, storeName, key, cls)
    }

    suspend fun removeDataByStringKey(context: Context, storeName: String, key: String) {
        baseTypeDataStorage.removeDataByKey(context, storeName, stringPreferencesKey(key))
    }

    suspend fun removeDataByFloatKey(context: Context, storeName: String, key: String) {
        baseTypeDataStorage.removeDataByKey(context, storeName, floatPreferencesKey(key))
    }

    suspend fun removeDataByBooleanKey(context: Context, storeName: String, key: String) {
        baseTypeDataStorage.removeDataByKey(context, storeName, booleanPreferencesKey(key))
    }

    suspend fun removeDataByLongKey(context: Context, storeName: String, key: String) {
        baseTypeDataStorage.removeDataByKey(context, storeName, longPreferencesKey(key))
    }

    suspend fun removeDataByIntKey(context: Context, storeName: String, key: String) {
        baseTypeDataStorage.removeDataByKey(context, storeName, intPreferencesKey(key))
    }

    suspend fun removeDataByStringSetKey(context: Context, storeName: String, key: String) {
        baseTypeDataStorage.removeDataByKey(context, storeName, stringSetPreferencesKey(key))
    }

    suspend fun removeDataByObjKey(context: Context, storeName: String, key: String) {
        baseTypeDataStorage.removeDataByKey(context, storeName, stringSetPreferencesKey(key))
    }

    suspend fun clearAllData(context: Context, storeName: String) {
        baseTypeDataStorage.clearAllData(context, storeName)
    }
}