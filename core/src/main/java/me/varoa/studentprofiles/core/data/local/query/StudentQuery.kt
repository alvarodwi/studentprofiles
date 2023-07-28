package me.varoa.studentprofiles.core.data.local.query

import androidx.sqlite.db.SimpleSQLiteQuery
import logcat.logcat

data class StudentQuery(
    val sort: SortKey = SortKey.Default,
    val sortDirection: SortDirectionKey = SortDirectionKey.Ascending,
    val filter: FilterKey = FilterKey(),
    val search: String = "",
) {
    fun generateQuery(): SimpleSQLiteQuery {
        val result =
            StringBuilder().apply {
                append("SELECT id, name, squadType, tacticRole, imgPath FROM students")
                append(" WHERE name LIKE '%$search%'")
                append(filter.getQuery())
                append(" ORDER BY ${sort.key} ${sortDirection.key}")
            }
        logcat { result.toString() }
        return SimpleSQLiteQuery(result.toString())
    }
}
