package me.varoa.studentprofiles.core.domain.usecase

import me.varoa.studentprofiles.core.data.local.entity.StudentEntity
import me.varoa.studentprofiles.core.domain.model.SyncInterval

interface SyncStudentUseCase {
    suspend fun insertStudent(vararg student: StudentEntity)

    suspend fun deleteAllStudent()

    suspend fun getSyncInterval(): SyncInterval
}
