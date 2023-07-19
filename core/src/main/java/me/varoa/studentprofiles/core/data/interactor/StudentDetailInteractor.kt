package me.varoa.studentprofiles.core.data.interactor

import kotlinx.coroutines.flow.Flow
import me.varoa.studentprofiles.core.domain.model.Student
import me.varoa.studentprofiles.core.domain.repository.FavoriteRepository
import me.varoa.studentprofiles.core.domain.repository.StudentRepository
import me.varoa.studentprofiles.core.domain.usecase.StudentDetailUseCase
import javax.inject.Inject

class StudentDetailInteractor
    @Inject
    constructor(
        private val studentRepository: StudentRepository,
        private val favoriteRepository: FavoriteRepository,
    ) : StudentDetailUseCase {
        override fun getStudent(id: Int): Flow<Student> = studentRepository.getStudent(id)

        override suspend fun addToFavorite(id: Int) = favoriteRepository.addToFavorite(id)

        override suspend fun removeFromFavorite(id: Int) = favoriteRepository.removeFromFavorite(id)
    }
