package me.varoa.studentprofiles.core.domain.usecase

import me.varoa.studentprofiles.core.data.local.entity.StudentEntity

interface SyncStudentUseCase {
    suspend fun insertStudent(vararg student: StudentEntity)

    suspend fun deleteAllStudent()
}
