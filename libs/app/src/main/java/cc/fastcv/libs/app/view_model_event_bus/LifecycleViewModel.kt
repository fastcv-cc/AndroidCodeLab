package cc.fastcv.libs.app.view_model_event_bus

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import cc.fastcv.logger.FastLogger

private const val TAG = "LifecycleViewModel"
class LifecycleViewModel(private val state:SavedStateHandle) : ViewModel() {


    fun getContent() {
        if (state.keys().isEmpty()) {
            FastLogger.i(TAG,"未缓存数据")
            return
        }
        for (key in state.keys()) {
            FastLogger.i(TAG,"缓存数据 key = $key   value = ${state.get<Any>(key)}")
        }
    }

    fun saveContent() {
        state.set("key1","我是一个值")
        state.set("key2",18)
        state.set("key3",true)
        FastLogger.i(TAG,"保存值")
    }

}
