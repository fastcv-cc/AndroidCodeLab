package cc.fastcv.libs.app.local_data_store

import android.content.Intent
import android.util.Log
import android.view.View
import androidx.lifecycle.lifecycleScope
import cc.fastcv.lab_base.LibItem
import cc.fastcv.lab_base.TreeActivity
import cc.fastcv.libs.app.local_data_store.multi_process_data_store.MultiProcessDataStoreActivity
import cc.fastcv.libs.app.local_data_store.preferences_data_store.PreferencesDataStoreActivity
import cc.fastcv.libs.app.local_data_store.proto_data_store.ProtoDataStoreActivity
import cc.fastcv.libs.app.local_data_store.shared_preferences.SharedPreferencesActivity
import cc.fastcv.local_data_store.LocalDataStore
import kotlinx.coroutines.launch

class LocalDataStoreActivity : TreeActivity() {

    companion object {
        private const val TAG = "LocalDataStoreActivity"
    }

    override fun buildLibItemList(): List<LibItem> {
        return listOf(
            LibItem(
                "SharedPreferences使用",
                "SharedPreferences使用",
                Intent(this, SharedPreferencesActivity::class.java)
            ),
            LibItem(
                "PreferencesDataStore使用",
                "PreferencesDataStore使用",
                Intent(this, PreferencesDataStoreActivity::class.java)
            ),
            LibItem(
                "ProtoDataStore使用",
                "ProtoDataStore使用",
                Intent(this, ProtoDataStoreActivity::class.java)
            ),
            LibItem(
                "MultiProcessDataStore使用",
                "MultiProcessDataStore使用",
                Intent(this, MultiProcessDataStoreActivity::class.java)
            ),
            LibItem("库功能测试", "库功能测试", Intent()),
        )
    }

    override fun onItemClick(view: View?, position: Int, t: LibItem) {
        if (position == 4) {
            testLocalDataStoreFunction()
        } else {
            super.onItemClick(view, position, t)
        }
    }


    private fun testLocalDataStoreFunction() {
        lifecycleScope.launch {
            val storeName = "localDataStore"
            val stringKey = "stringKey"
            LocalDataStore.saveString(
                this@LocalDataStoreActivity,
                storeName,
                stringKey,
                "我是一个值"
            )
            val stringValue = LocalDataStore.getString(
                this@LocalDataStoreActivity,
                storeName,
                stringKey,
                "我是一个默认值"
            )
            Log.d(TAG, "testLocalDataStoreFunction: stringValue = $stringValue")
            val objKey = "objKey"
            LocalDataStore.saveObj(
                this@LocalDataStoreActivity,
                storeName,
                objKey,
                LocalDataStoreEntity("Hello", 20)
            )
            val objValue = LocalDataStore.getObj(
                this@LocalDataStoreActivity,
                storeName,
                objKey, LocalDataStoreEntity::class.java
            )
            Log.d(TAG, "testLocalDataStoreFunction: objValue = $objValue")
        }

    }

}

private data class LocalDataStoreEntity(val name: String, val age: Int)