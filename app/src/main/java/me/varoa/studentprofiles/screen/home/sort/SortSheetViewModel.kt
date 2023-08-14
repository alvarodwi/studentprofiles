package me.varoa.studentprofiles.screen.home.sort

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import me.varoa.studentprofiles.core.data.local.query.SortKey

class SortSheetViewModel : ViewModel() {
    data class UiState(
        val sortKey: SortKey = SortKey.Default,
    )

    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    val sortKeys = enumValues<SortKey>()

    fun updateSort(sortKey: SortKey) {
        _uiState.update { it.copy(sortKey = sortKey) }
    }
}
