package cc.fastcv.viewmodel_event_bus.core

abstract class SingleEvent {

    private var abandoned = false

    fun abandoned() {
        abandoned = true
    }

    fun isAbandoned() = abandoned

}