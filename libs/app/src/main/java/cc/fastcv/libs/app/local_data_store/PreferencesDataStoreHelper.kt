package cc.fastcv.libs.app.local_data_store

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

object PreferencesDataStoreHelper {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
        name = "PreferencesDataStoreActivity"
    )

    suspend fun put(context: Context, key: String, value: String) {
        context.dataStore.edit {
            it[stringPreferencesKey(key)] = value
        }
    }

    suspend fun put(context: Context, key: String, value: Int) {
        context.dataStore.edit {
            it[intPreferencesKey(key)] = value
        }
    }

    suspend fun put(context: Context, key: String, value: Long) {
        context.dataStore.edit {
            it[longPreferencesKey(key)] = value
        }
    }

    suspend fun put(context: Context, key: String, value: Float) {
        context.dataStore.edit {
            it[floatPreferencesKey(key)] = value
        }
    }

    suspend fun put(context: Context, key: String, value: Boolean) {
        context.dataStore.edit {
            it[booleanPreferencesKey(key)] = value
        }
    }

    suspend fun put(context: Context, key: String, value: Set<String>) {
        context.dataStore.edit {
            it[stringSetPreferencesKey(key)] = value
        }
    }

    fun getStringFlow(context: Context, key: String, defaultValue: String): Flow<String> {
        return context.dataStore.data.map { it[stringPreferencesKey(key)] ?: defaultValue }
    }

    fun getInt(context: Context, key: String, defaultValue: Int): Flow<Int> {
        return context.dataStore.data.map { it[intPreferencesKey(key)] ?: defaultValue }
    }

    fun getLong(context: Context, key: String, defaultValue: Long): Flow<Long> {
        return context.dataStore.data.map { it[longPreferencesKey(key)] ?: defaultValue }
    }

    fun getFloat(context: Context, key: String, defaultValue: Float): Flow<Float> {
        return context.dataStore.data.map { it[floatPreferencesKey(key)] ?: defaultValue }
    }

    fun getBoolean(context: Context, key: String, defaultValue: Boolean): Flow<Boolean> {
        return context.dataStore.data.map { it[booleanPreferencesKey(key)] ?: defaultValue }
    }

    fun getStringSet(
        context: Context,
        key: String,
        defaultValue: Set<String>
    ): Flow<Set<String>> {
        return context.dataStore.data.map { it[stringSetPreferencesKey(key)] ?: defaultValue }

    }

    suspend fun <T> contains(context: Context, key: Preferences.Key<T>): Boolean {
        return context.dataStore.data.map { it.contains(key) }.first()
    }

    suspend fun <T> remove(context: Context, key: Preferences.Key<T>) {
        context.dataStore.edit {
            it.remove(key)
        }
    }

    suspend fun clear(context: Context) {
        context.dataStore.edit {
            it.clear()
        }
    }
}