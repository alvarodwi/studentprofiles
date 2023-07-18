package me.varoa.studentprofiles.base

open class UiEvent {
    object Loading : UiEvent()
    object NotLoading : UiEvent()
    data class Error(val throwable: Throwable?) : UiEvent()
}
