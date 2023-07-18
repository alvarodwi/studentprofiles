package me.varoa.studentprofiles.core.data.local.query

import androidx.sqlite.db.SimpleSQLiteQuery
import logcat.logcat
import me.varoa.studentprofiles.core.data.local.query.SortKey.Name

class StudentQuery {
    var sort: SortKey = Name
    var sortDirection: SortDirectionKey = SortDirectionKey.Ascending
    var filter: FilterKey = FilterKey()
    var query: String = ""

    fun generateQuery(): String {
        val result = StringBuilder().apply {
            append("SELECT devName, name FROM students")
            append(" WHERE name LIKE '%$query%'")
            append(filter.getQuery())
            append(" ORDER BY ${sort.key} ${sortDirection.key}")
        }
        logcat { result.toString() }
        return result.toString()
    }
}
