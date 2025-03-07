package cc.fastcv.local_data_store

import android.content.Context
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class BaseTypeDataStorageUtils {

    /********************* String *********************/
    suspend fun saveString(context: Context, storeName: String, key: String, value: String) {
        val dataStore = preferencesDataStore(context = context, name = storeName)
        dataStore.edit { it[stringPreferencesKey(key)] = value }
    }

    suspend fun getString(context: Context, storeName: String, key: String, default: String) : String {
        val dataStore = preferencesDataStore(context = context, name = storeName)
        return dataStore.data.map { it[stringPreferencesKey(key)] ?: default }.first()
    }

    /********************* Float *********************/
    suspend fun saveFloat(context: Context, storeName: String, key: String, value: Float) {
        val dataStore = preferencesDataStore(context = context, name = storeName)
        dataStore.edit { it[floatPreferencesKey(key)] = value }
    }

    suspend fun getFloat(context: Context, storeName: String, key: String, default: Float) : Float {
        val dataStore = preferencesDataStore(context = context, name = storeName)
        return dataStore.data.map { it[floatPreferencesKey(key)] ?: default }.first()
    }

    /********************* Boolean *********************/
    suspend fun saveBoolean(context: Context, storeName: String, key: String, value: Boolean) {
        val dataStore = preferencesDataStore(context = context, name = storeName)
        dataStore.edit { it[booleanPreferencesKey(key)] = value }
    }

    suspend fun getBoolean(context: Context, storeName: String, key: String, default: Boolean) : Boolean {
        val dataStore = preferencesDataStore(context = context, name = storeName)
        return dataStore.data.map { it[booleanPreferencesKey(key)] ?: default }.first()
    }

    /********************* Long *********************/
    suspend fun saveLong(context: Context, storeName: String, key: String, value: Long) {
        val dataStore = preferencesDataStore(context = context, name = storeName)
        dataStore.edit { it[longPreferencesKey(key)] = value }
    }

    suspend fun getLong(context: Context, storeName: String, key: String, default: Long) : Long {
        val dataStore = preferencesDataStore(context = context, name = storeName)
        return dataStore.data.map { it[longPreferencesKey(key)] ?: default }.first()
    }

    /********************* Int *********************/
    suspend fun saveInt(context: Context, storeName: String, key: String, value: Int) {
        val dataStore = preferencesDataStore(context = context, name = storeName)
        dataStore.edit { it[intPreferencesKey(key)] = value }
    }

    suspend fun getInt(context: Context, storeName: String, key: String, default: Int) : Int {
        val dataStore = preferencesDataStore(context = context, name = storeName)
        return dataStore.data.map { it[intPreferencesKey(key)] ?: default }.first()
    }

    /********************* StringSet *********************/
    suspend fun saveStringSet(context: Context, storeName: String, key: String, value: Set<String>) {
        val dataStore = preferencesDataStore(context = context, name = storeName)
        dataStore.edit { it[stringSetPreferencesKey(key)] = value }
    }

    suspend fun getStringSet(context: Context, storeName: String, key: String, default: Set<String>) : Set<String> {
        val dataStore = preferencesDataStore(context = context, name = storeName)
        return dataStore.data.map { it[stringSetPreferencesKey(key)] ?: default }.first()
    }

    /********************* remove key *********************/
    suspend fun <T> removeDataByKey(context: Context, storeName: String, key: Preferences.Key<T>) {
        val dataStore = preferencesDataStore(context = context, name = storeName)
        dataStore.edit { it.remove(key) }
    }

    /********************* remove data store by name *********************/
    suspend fun clearAllData(context: Context, storeName: String) {
        val dataStore = preferencesDataStore(context = context, name = storeName)
        dataStore.edit { it.clear() }
    }
}