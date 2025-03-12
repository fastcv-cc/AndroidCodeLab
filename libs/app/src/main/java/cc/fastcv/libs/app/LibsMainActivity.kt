package cc.fastcv.libs.app

import android.content.Intent
import cc.fastcv.lab_base.LibItem
import cc.fastcv.lab_base.TreeActivity
import cc.fastcv.libs.app.local_data_store.LocalDataStoreActivity
import cc.fastcv.libs.app.logger.LoggerActivity
import cc.fastcv.libs.app.view_model_event_bus.ViewModelEventBusActivity

class LibsMainActivity : TreeActivity() {
    override fun buildLibItemList(): List<LibItem> {
        return listOf(
            LibItem("Logger库", "一个日志打印库", Intent(this, LoggerActivity::class.java)),
            LibItem("LocalDataStore库", "本地轻量化数据持久化库", Intent(this, LocalDataStoreActivity::class.java)),
            LibItem("ViewModelEventBus库", "使用ViewModel结合Flow实现的事件总线库", Intent(this, ViewModelEventBusActivity::class.java)),
        )
    }
}