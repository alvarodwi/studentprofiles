package me.varoa.studentprofiles.core.data.interactor

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import me.varoa.studentprofiles.core.domain.model.StudentMinified
import me.varoa.studentprofiles.core.domain.repository.FavoriteRepository
import me.varoa.studentprofiles.core.domain.usecase.FavoriteListUseCase
import javax.inject.Inject

class FavoriteListInteractor
    @Inject
    constructor(
        private val repository: FavoriteRepository,
    ) : FavoriteListUseCase {
        override fun getFavorites(): Flow<PagingData<StudentMinified>> = repository.getFavorites()
    }
