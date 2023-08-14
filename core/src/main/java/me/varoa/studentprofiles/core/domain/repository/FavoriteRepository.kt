package me.varoa.studentprofiles.core.domain.repository

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import me.varoa.studentprofiles.core.domain.model.StudentMinified

interface FavoriteRepository {
    fun getFavorites(): Flow<PagingData<StudentMinified>>

    suspend fun addToFavorite(id: Int)

    suspend fun removeFromFavorite(id: Int)
}
