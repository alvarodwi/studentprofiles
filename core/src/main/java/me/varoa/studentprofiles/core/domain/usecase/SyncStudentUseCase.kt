package me.varoa.studentprofiles.core.domain.usecase

import me.varoa.studentprofiles.core.domain.model.Student
import me.varoa.studentprofiles.core.domain.model.SyncInterval

interface SyncStudentUseCase {
    suspend fun insertStudent(vararg student: Student)

    suspend fun deleteAllStudent()

    suspend fun getSyncInterval(): SyncInterval
}
