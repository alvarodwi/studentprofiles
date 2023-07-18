package me.varoa.studentprofiles.core.domain.model

import me.varoa.studentprofiles.core.domain.model.AttackType.Explosive

enum class TacticRole(val key: String) {
    Dealer("DamageDealer"),
    Healer("Healer"),
    Supporter("Supporter"),
    Tank("Tanker"),
    TS("Vehicle");

    companion object {
        private val map: Map<String, TacticRole> = TacticRole.values().associateBy { it.key }
        infix fun from(key: String) = map[key] ?: Dealer
    }
}
