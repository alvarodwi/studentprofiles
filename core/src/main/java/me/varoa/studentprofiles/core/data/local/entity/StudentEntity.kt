package me.varoa.studentprofiles.core.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("students")
data class StudentEntity(
    @PrimaryKey val id: Int,
    val releasedInGlobal: Boolean,
    val name: String,
    val squadType: String,
    val tacticRole: String,
    val attackType: String,
    val defenseType: String,
    val weaponType: String,
    val position: String,
    val school: String,
    val imgPath: String,
    // profile
    val fullName: String,
    val club: String,
    val schoolYear: String,
    val basicInfo: String,
    val age: String,
    val birthday: String,
    val height: String,
    val hobbies: String,
    val designer: String,
    val illustrator: String,
    val cv: String,
    // img
    val devName: String,
    val bgImgPath: String,
    val weaponImgPath: String,
    // feature
    val isFavorite: Boolean = false,
)
