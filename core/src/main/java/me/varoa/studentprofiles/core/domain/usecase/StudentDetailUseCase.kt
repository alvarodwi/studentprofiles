package me.varoa.studentprofiles.core.domain.usecase

import kotlinx.coroutines.flow.Flow
import me.varoa.studentprofiles.core.domain.model.Student

interface StudentDetailUseCase {
    fun getStudent(devName: String): Flow<Student>
    suspend fun addToFavorite(devName: String)
    suspend fun removeFromFavorite(devName: String)
}
