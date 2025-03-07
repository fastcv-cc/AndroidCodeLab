package cc.fastcv.libs.app.local_data_store

import android.content.SharedPreferences
import android.content.SharedPreferences.OnSharedPreferenceChangeListener
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import cc.fastcv.libs.app.R

private const val TAG = "SharedPreferences"

class SharedPreferencesActivity : AppCompatActivity(), OnSharedPreferenceChangeListener {

    private val spName = "SharedPreferencesActivity"
    private val keyString = "SpKeyString"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shared_preferences)

        findViewById<Button>(R.id.bt1).setOnClickListener {
            SharedPreferencesHelper.put(
                this,
                spName,
                keyString,
                "这是一个值 ${System.currentTimeMillis()}"
            )
        }

        findViewById<Button>(R.id.bt2).setOnClickListener {
            val string = SharedPreferencesHelper.getString(this, spName, keyString, "默认值")
            Log.d(TAG, "get value: $string")
        }

        findViewById<Button>(R.id.bt3).setOnClickListener {
            getSharedPreferences(
                spName,
                MODE_PRIVATE
            ).registerOnSharedPreferenceChangeListener(this)
        }

        findViewById<Button>(R.id.bt4).setOnClickListener {
            SharedPreferencesHelper.remove(this, spName, keyString)
        }
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        Log.d(
            TAG,
            "onSharedPreferenceChanged: key = $key   value = ${
                sharedPreferences?.getString(
                    key,
                    "默认值"
                )
            }"
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        getSharedPreferences(spName, MODE_PRIVATE).unregisterOnSharedPreferenceChangeListener(this)
    }

}