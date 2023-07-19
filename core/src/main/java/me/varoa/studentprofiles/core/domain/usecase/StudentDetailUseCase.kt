package me.varoa.studentprofiles.core.domain.usecase

import kotlinx.coroutines.flow.Flow
import me.varoa.studentprofiles.core.domain.model.Student

interface StudentDetailUseCase {
    fun getStudent(id: Int): Flow<Student>
    suspend fun addToFavorite(id: Int)
    suspend fun removeFromFavorite(id: Int)
}
