package me.varoa.studentprofiles.core.domain.repository

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import me.varoa.studentprofiles.core.data.local.entity.StudentEntity
import me.varoa.studentprofiles.core.data.local.query.StudentQuery
import me.varoa.studentprofiles.core.domain.model.Student
import me.varoa.studentprofiles.core.domain.model.StudentMinified

interface StudentRepository {
    fun getStudents(query: StudentQuery): Flow<PagingData<StudentMinified>>

    fun getStudent(id: Int): Flow<Student>

    suspend fun insertStudent(vararg student: StudentEntity)

    suspend fun deleteAllStudent()
}
