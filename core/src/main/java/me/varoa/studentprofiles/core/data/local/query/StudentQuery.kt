package me.varoa.studentprofiles.core.data.local.query

import androidx.sqlite.db.SimpleSQLiteQuery
import logcat.logcat

class StudentQuery {
    var sort: SortKey = SortKey.Name
    var sortDirection: SortDirectionKey = SortDirectionKey.Ascending
    var filter: FilterKey = FilterKey()
    var query: String = ""

    fun generateQuery(): SimpleSQLiteQuery {
        val result =
            StringBuilder().apply {
                append("SELECT name, squadType, tacticRole, imgPath FROM students")
                append(" WHERE name LIKE '%$query%'")
                append(filter.getQuery())
                append(" ORDER BY ${sort.key} ${sortDirection.key}")
            }
        logcat { result.toString() }
        return SimpleSQLiteQuery(result.toString())
    }
}
