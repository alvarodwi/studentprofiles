package me.varoa.studentprofiles.core.domain.model

enum class TacticRole(val key: String) {
    Dealer("DamageDealer"),
    Healer("Healer"),
    Supporter("Supporter"),
    Tank("Tanker"),
    TS("Vehicle"),
    ;

    companion object {
        private val MAP: Map<String, TacticRole> = TacticRole.values().associateBy { it.key }
        infix fun from(key: String) = MAP[key] ?: Dealer
    }
}
