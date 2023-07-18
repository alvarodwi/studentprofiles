package me.varoa.studentprofiles.core.domain.model

enum class AttackType(val key: String) {
    Explosive("Explosion"),
    Piercing("Pierce"),
    Mystic("Mystic");

    companion object {
        private val map: Map<String, AttackType> = AttackType.values().associateBy { it.key }
        infix fun from(key: String) = map[key] ?: Explosive
    }
}
