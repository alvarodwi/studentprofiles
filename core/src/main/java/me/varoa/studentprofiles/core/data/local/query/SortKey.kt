package me.varoa.studentprofiles.core.data.local.query

enum class SortKey(val key: String) {
    Default("defaultOrder"),
    Name("name"),
    SquadType("squadType"),
    Role("tacticRole"),
    AttackType("attackType"),
    DefenseType("defenseType"),
    WeaponType("weaponType"),
    Academy("school"),
}
