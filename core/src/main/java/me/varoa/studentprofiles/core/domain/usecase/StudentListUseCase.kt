package me.varoa.studentprofiles.core.domain.usecase

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import me.varoa.studentprofiles.core.data.local.query.StudentQuery
import me.varoa.studentprofiles.core.domain.model.StudentMinified

interface StudentListUseCase {
    fun getStudents(query: StudentQuery): Flow<PagingData<StudentMinified>>
}
