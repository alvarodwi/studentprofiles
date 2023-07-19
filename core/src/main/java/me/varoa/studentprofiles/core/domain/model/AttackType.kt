package me.varoa.studentprofiles.core.domain.model

enum class AttackType(val key: String) {
    Explosive("Explosion"),
    Piercing("Pierce"),
    Mystic("Mystic"),
    ;

    companion object {
        private val MAP: Map<String, AttackType> = AttackType.values().associateBy { it.key }
        infix fun from(key: String) = MAP[key] ?: Explosive
    }
}
