package me.varoa.studentprofiles.core.data.interactor

import kotlinx.coroutines.flow.first
import me.varoa.studentprofiles.core.data.local.entity.StudentEntity
import me.varoa.studentprofiles.core.data.prefs.DataStoreManager
import me.varoa.studentprofiles.core.domain.model.SyncInterval
import me.varoa.studentprofiles.core.domain.repository.StudentRepository
import me.varoa.studentprofiles.core.domain.usecase.SyncStudentUseCase

class SyncStudentInteractor(
    private val repository: StudentRepository,
    private val prefs: DataStoreManager,
) : SyncStudentUseCase {
    override suspend fun insertStudent(vararg student: StudentEntity) = repository.insertStudent(*student)

    override suspend fun deleteAllStudent() = repository.deleteAllStudent()

    override suspend fun getSyncInterval(): SyncInterval = SyncInterval.valueOf(prefs.syncInterval.first())
}
