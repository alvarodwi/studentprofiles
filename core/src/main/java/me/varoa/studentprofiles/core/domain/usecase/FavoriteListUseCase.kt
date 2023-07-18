package me.varoa.studentprofiles.core.domain.usecase

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import me.varoa.studentprofiles.core.domain.model.StudentMinified

interface FavoriteListUseCase {
    fun getFavorites() : Flow<PagingData<StudentMinified>>
}
