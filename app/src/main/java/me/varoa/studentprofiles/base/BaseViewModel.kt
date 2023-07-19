package me.varoa.studentprofiles.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

abstract class BaseViewModel : ViewModel() {
    private val mutableEvents = Channel<UiEvent>()
    val events: Flow<UiEvent> = mutableEvents.receiveAsFlow()

    protected var apiJob: Job? = null

    protected fun UiEvent.send() {
        viewModelScope.launch { mutableEvents.send(this@send) }
    }
}
