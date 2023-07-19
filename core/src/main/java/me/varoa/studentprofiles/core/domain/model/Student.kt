package me.varoa.studentprofiles.core.domain.model

data class Student(
    val id: Int,
    val releasedInGlobal: Boolean,
    val name: String,
    val imgPath: String,
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
