package cc.fastcv.local_data_store

import android.content.Context
import androidx.annotation.GuardedBy
import androidx.datastore.core.DataMigration
import androidx.datastore.core.DataStore
import androidx.datastore.core.handlers.ReplaceFileCorruptionHandler
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

private val dataStoreMap = mutableMapOf<String,DataStore<Preferences>>()

fun preferencesDataStore(
    context: Context,
    name: String,
    corruptionHandler: ReplaceFileCorruptionHandler<Preferences>? = null,
    produceMigrations: (Context) -> List<DataMigration<Preferences>> = { listOf() },
    scope: CoroutineScope = CoroutineScope(Dispatchers.IO + SupervisorJob())
): DataStore<Preferences> {
    var dataStore = dataStoreMap[name]
    if (dataStore == null) {
        dataStore = MyPreferenceDataStoreSingletonDelegate(
            name,
            corruptionHandler,
            produceMigrations,
            scope
        ).getValue(context)
        dataStoreMap[name] = dataStore
    }
    return dataStore
}

class MyPreferenceDataStoreSingletonDelegate internal constructor(
    private val name: String,
    private val corruptionHandler: ReplaceFileCorruptionHandler<Preferences>?,
    private val produceMigrations: (Context) -> List<DataMigration<Preferences>>,
    private val scope: CoroutineScope
) {

    private val lock = Any()

    @GuardedBy("lock")
    @Volatile
    private var instance: DataStore<Preferences>? = null

    fun getValue(thisRef: Context): DataStore<Preferences> {
        return instance ?: synchronized(lock) {
            if (instance == null) {
                val applicationContext = thisRef.applicationContext

                instance = PreferenceDataStoreFactory.create(
                    corruptionHandler = corruptionHandler,
                    migrations = produceMigrations(applicationContext),
                    scope = scope
                ) {
                    applicationContext.preferencesDataStoreFile(name)
                }
            }
            instance!!
        }
    }
}