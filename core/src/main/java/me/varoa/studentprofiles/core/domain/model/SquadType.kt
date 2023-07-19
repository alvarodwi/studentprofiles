package me.varoa.studentprofiles.core.domain.model

enum class SquadType(val key: String) {
    Striker("Main"),
    Special("Support"),
    ;

    companion object {
        private val MAP: Map<String, SquadType> = SquadType.values().associateBy { it.key }
        infix fun from(key: String) = MAP[key] ?: Striker
    }
}
