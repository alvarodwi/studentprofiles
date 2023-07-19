package me.varoa.studentprofiles.screen.home

import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.update
import me.varoa.studentprofiles.base.BaseViewModel
import me.varoa.studentprofiles.core.data.local.query.StudentQuery
import me.varoa.studentprofiles.core.domain.usecase.StudentListUseCase
import javax.inject.Inject

@HiltViewModel
class HomeViewModel
    @Inject
    constructor(
        private val useCase: StudentListUseCase,
    ) : BaseViewModel() {
        private val _query: MutableStateFlow<StudentQuery> = MutableStateFlow(StudentQuery())
        val query = _query.asStateFlow()

        val students =
            query.flatMapLatest {
                useCase.getStudents(it)
            }

        fun updateQuery(newQuery: StudentQuery) {
            _query.update { newQuery }
        }
    }
