package me.varoa.studentprofiles.core.domain.model

enum class DefenseType(val key : String) {
    Light("LightArmor"),
    Heavy("HeavyArmor"),
    Special("Unarmed");

    companion object {
        private val map: Map<String, DefenseType> = DefenseType.values().associateBy { it.key }
        infix fun from(key: String) = map[key] ?: Light
    }
}
