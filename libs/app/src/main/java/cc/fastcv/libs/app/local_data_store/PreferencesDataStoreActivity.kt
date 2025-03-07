package cc.fastcv.libs.app.local_data_store

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.SharedPreferencesMigration
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.core.stringSetPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.lifecycleScope
import cc.fastcv.libs.app.R
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

private const val TAG = "PreferencesDataStore"

class PreferencesDataStoreActivity : AppCompatActivity() {

    private val keyString = "pfKeyString"

    private val Context.dataStoreBySp: DataStore<Preferences> by preferencesDataStore(name = "dataStoreBySp", produceMigrations = { context ->
        listOf(SharedPreferencesMigration(context,"SharedPreferencesActivity"))
    })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_preferences_data_store)

        findViewById<Button>(R.id.bt1).setOnClickListener {
            lifecycleScope.launch {
                PreferencesDataStoreHelper.put(
                    this@PreferencesDataStoreActivity,
                    keyString,
                    "这是一个值 ${System.currentTimeMillis()}"
                )
            }
        }

        findViewById<Button>(R.id.bt2).setOnClickListener {
            lifecycleScope.launch {
                val string = PreferencesDataStoreHelper.getStringFlow(
                    this@PreferencesDataStoreActivity,
                    keyString,
                    "默认值"
                ).first()
                Log.d(TAG, "get value: $string")
            }
        }

        findViewById<Button>(R.id.bt3).setOnClickListener {
            lifecycleScope.launch {
                PreferencesDataStoreHelper.getStringFlow(
                    this@PreferencesDataStoreActivity,
                    keyString,
                    "默认值"
                ).collectLatest {
                    Log.d(
                        TAG,
                        "SpKeyString对应的value变化 value = $it"
                    )
                }
            }
        }

        findViewById<Button>(R.id.bt4).setOnClickListener {
            lifecycleScope.launch {
                PreferencesDataStoreHelper.remove(
                    this@PreferencesDataStoreActivity,
                    stringSetPreferencesKey(keyString)
                )
            }
        }

        findViewById<Button>(R.id.bt5).setOnClickListener {
            lifecycleScope.launch {
                dataStoreBySp.data.map {
                    it[stringPreferencesKey("SpKeyString")]
                }.collectLatest {
                    Log.d(TAG, "读取到值 $it")
                }
            }
        }
    }

}