package me.varoa.studentprofiles.core.domain.model

enum class DefenseType(val key: String) {
    Light("LightArmor"),
    Heavy("HeavyArmor"),
    Special("Unarmed"),
    ;

    companion object {
        private val MAP: Map<String, DefenseType> = DefenseType.values().associateBy { it.key }
        infix fun from(key: String) = MAP[key] ?: Light
    }
}
