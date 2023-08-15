package me.varoa.studentprofiles.screen.home.filter

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import me.varoa.studentprofiles.core.data.local.query.FilterKey

class FilterSheetViewModel : ViewModel() {
    data class UiState(
        val filterKey: FilterKey = FilterKey(),
        val canReset: Boolean = false,
    )

    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    fun updateFilter(filterKey: FilterKey) {
        _uiState.update { it.copy(filterKey = filterKey, canReset = !filterKey.isUnfiltered()) }
    }

    fun updateCanReset(flag: Boolean) {
        _uiState.update { it.copy(canReset = flag) }
    }

    fun reset() {
        _uiState.update { it.copy(filterKey = FilterKey(), canReset = false) }
    }
}
