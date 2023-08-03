package me.varoa.studentprofiles.core.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import me.varoa.studentprofiles.core.data.local.dao.FavoriteDao
import me.varoa.studentprofiles.core.domain.model.StudentMinified
import me.varoa.studentprofiles.core.domain.repository.FavoriteRepository

class FavoriteRepositoryImpl(
    private val dao: FavoriteDao,
) : FavoriteRepository {
    override fun getFavorites(): Flow<PagingData<StudentMinified>> =
        Pager(PagingConfig(pageSize = 20)) {
            dao.getFavorites()
        }.flow

    override suspend fun addToFavorite(id: Int) = dao.updateFavorite(id, true)

    override suspend fun removeFromFavorite(id: Int) = dao.updateFavorite(id, false)
}
