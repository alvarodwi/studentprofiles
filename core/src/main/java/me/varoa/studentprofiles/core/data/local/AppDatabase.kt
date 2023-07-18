package me.varoa.studentprofiles.core.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import me.varoa.studentprofiles.core.data.local.dao.FavoriteDao
import me.varoa.studentprofiles.core.data.local.dao.StudentDao
import me.varoa.studentprofiles.core.data.local.entity.StudentEntity

@Database(version = 1, entities = [StudentEntity::class])
abstract class AppDatabase : RoomDatabase() {
    abstract val studentDao: StudentDao
    abstract val favoriteDao: FavoriteDao
}
