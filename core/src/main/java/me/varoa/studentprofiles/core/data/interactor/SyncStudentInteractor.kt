package me.varoa.studentprofiles.core.data.interactor

import me.varoa.studentprofiles.core.data.local.entity.StudentEntity
import me.varoa.studentprofiles.core.domain.repository.StudentRepository
import me.varoa.studentprofiles.core.domain.usecase.SyncStudentUseCase
import javax.inject.Inject

class SyncStudentInteractor @Inject constructor(
    private val repository: StudentRepository
) : SyncStudentUseCase {
    override suspend fun insertStudent(vararg student: StudentEntity) = repository.insertStudent(*student)

    override suspend fun deleteAllStudent() = repository.deleteAllStudent()
}
