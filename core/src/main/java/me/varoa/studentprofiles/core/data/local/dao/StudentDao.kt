package me.varoa.studentprofiles.core.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RawQuery
import androidx.sqlite.db.SupportSQLiteQuery
import kotlinx.coroutines.flow.Flow
import me.varoa.studentprofiles.core.data.local.entity.StudentEntity
import me.varoa.studentprofiles.core.domain.model.StudentMinified

@Dao
interface StudentDao {
    @RawQuery(observedEntities = [StudentEntity::class])
    fun getAll(query: SupportSQLiteQuery): PagingSource<Int, StudentMinified>

    @Query("select * from students where devName = :devName")
    fun getStudent(devName: String): Flow<StudentEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg students: StudentEntity)

    @Query("delete from students")
    fun deleteAll()
}
