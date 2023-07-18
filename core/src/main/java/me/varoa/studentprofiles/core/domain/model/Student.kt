package me.varoa.studentprofiles.core.domain.model

data class Student(
    val releasedInGlobal: Boolean,
    val name: String,
    val devName: String,
    val squadType: SquadType,
    val tacticRole: TacticRole,
    val attackType: AttackType,
    val defenseType: DefenseType,
    val weaponType: WeaponType,
    val position: StudentPosition,
    val school: School,
    val profile: StudentProfile,
    val isFavorite: Boolean,
)
