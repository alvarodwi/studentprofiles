package me.varoa.studentprofiles.core.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Query
import me.varoa.studentprofiles.core.domain.model.StudentMinified

@Dao
interface FavoriteDao {
    @Query("SELECT devName, name FROM students WHERE isFavorite = 1")
    fun getFavorites(): PagingSource<Int, StudentMinified>

    @Query("UPDATE students SET isFavorite = :isFavorite WHERE devName = :devName")
    fun updateFavorite(devName: String, isFavorite: Boolean)
}
