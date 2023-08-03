package me.varoa.studentprofiles.screen.home

import androidx.lifecycle.ViewModel
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.update
import me.varoa.studentprofiles.core.data.local.query.FilterKey
import me.varoa.studentprofiles.core.data.local.query.SortDirectionKey
import me.varoa.studentprofiles.core.data.local.query.SortKey
import me.varoa.studentprofiles.core.data.local.query.StudentQuery
import me.varoa.studentprofiles.core.domain.model.StudentMinified
import me.varoa.studentprofiles.core.domain.usecase.StudentListUseCase

class HomeViewModel(
    private val useCase: StudentListUseCase,
) : ViewModel() {
    private val _query: MutableStateFlow<StudentQuery> = MutableStateFlow(StudentQuery())
    val query = _query.asStateFlow()

    val students: Flow<PagingData<StudentMinified>> =
        query.flatMapLatest {
            useCase.getStudents(it)
        }

    fun search(query: String) {
        _query.update { it.copy(search = query) }
    }

    fun updateSortDirection() {
        val newSortDirection =
            when (_query.value.sortDirection) {
                SortDirectionKey.Ascending -> SortDirectionKey.Descending
                else -> SortDirectionKey.Ascending
            }
        _query.update { it.copy(sortDirection = newSortDirection) }
    }

    fun updateSort(sort: SortKey) {
        _query.update { it.copy(sort = sort) }
    }

    fun updateFilter(filter: FilterKey) {
        _query.update { it.copy(filter = filter) }
    }
}
