package me.varoa.studentprofiles.core.data.local.query

import me.varoa.studentprofiles.core.domain.model.AttackType
import me.varoa.studentprofiles.core.domain.model.DefenseType
import me.varoa.studentprofiles.core.domain.model.School
import me.varoa.studentprofiles.core.domain.model.SquadType
import me.varoa.studentprofiles.core.domain.model.StudentPosition
import me.varoa.studentprofiles.core.domain.model.TacticRole
import me.varoa.studentprofiles.core.domain.model.WeaponType

data class FilterKey(
    val isGlobalServer: Boolean = true,
    val squadType: SquadType? = null,
    val tacticRole: TacticRole? = null,
    val attackType: AttackType? = null,
    val defenseType: DefenseType? = null,
    val weaponType: WeaponType? = null,
    val position: StudentPosition? = null,
    val school: School? = null,
) {
    private val baseQuery = " AND releasedInGlobal = ${if(isGlobalServer) 1 else 0}"

    private fun StringBuilder.generateFilterQuery() {
        squadType?.let {
            append(" AND squadType = '${squadType.key}'")
        }
        tacticRole?.let {
            append(" AND tacticRole = '${tacticRole.key}'")
        }
        attackType?.let {
            append(" AND attackType = '${attackType.key}'")
        }
        defenseType?.let {
            append(" AND defenseType = '${defenseType.key}'")
        }
        weaponType?.let {
            append(" AND weaponType = '$weaponType'")
        }
        position?.let {
            append(" AND position = '$position'")
        }
        school?.let {
            append(" AND school = '${school.key}'")
        }
    }

    fun getQuery(): String {
        return StringBuilder().apply {
            append(baseQuery)
            generateFilterQuery()
        }.toString()
    }
}

