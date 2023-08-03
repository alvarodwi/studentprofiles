package me.varoa.studentprofiles.core.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import me.varoa.studentprofiles.core.data.local.dao.StudentDao
import me.varoa.studentprofiles.core.data.local.entity.StudentEntity
import me.varoa.studentprofiles.core.data.local.query.StudentQuery
import me.varoa.studentprofiles.core.domain.model.Student
import me.varoa.studentprofiles.core.domain.model.StudentMinified
import me.varoa.studentprofiles.core.domain.repository.StudentRepository
import me.varoa.studentprofiles.core.util.asModel

class StudentRepositoryImpl(
    private val dao: StudentDao,
) : StudentRepository {
    override fun getStudents(query: StudentQuery): Flow<PagingData<StudentMinified>> =
        Pager(PagingConfig(pageSize = 20)) {
            dao.getAll(query.generateQuery())
        }.flow

    override fun getStudent(id: Int): Flow<Student> = dao.getStudent(id).map(StudentEntity::asModel)

    override suspend fun insertStudent(vararg student: StudentEntity) = dao.insert(*student)

    override suspend fun deleteAllStudent() = dao.deleteAll()
}
