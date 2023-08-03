package me.varoa.studentprofiles.core.data.interactor

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import me.varoa.studentprofiles.core.data.local.query.StudentQuery
import me.varoa.studentprofiles.core.domain.model.StudentMinified
import me.varoa.studentprofiles.core.domain.repository.StudentRepository
import me.varoa.studentprofiles.core.domain.usecase.StudentListUseCase

class StudentListInteractor(
    private val repository: StudentRepository,
) : StudentListUseCase {
    override fun getStudents(query: StudentQuery): Flow<PagingData<StudentMinified>> = repository.getStudents(query)
}
