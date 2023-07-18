package me.varoa.studentprofiles.core.data.remote.json

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StudentJson(
    @SerialName("IsReleased") val isReleased: List<Boolean>,
    @SerialName("Name") val name: String,
    @SerialName("SquadType") val squadType: String,
    @SerialName("TacticRole") val tacticRole: String,
    @SerialName("BulletType") val attackType: String,
    @SerialName("ArmorType") val defenseType: String,
    @SerialName("WeaponType") val weaponType: String,
    @SerialName("Position") val position: String,
    @SerialName("School") val school: String,
    // profile
    @SerialName("FamilyName") val familyName: String,
    @SerialName("PersonalName") val personalName: String,
    @SerialName("Club") val club: String,
    @SerialName("SchoolYear") val schoolYear: String,
    @SerialName("ProfileIntroduction") val basicInfo: String,
    @SerialName("CharacterAge") val age: String,
    @SerialName("BirthDay") val birthday: String,
    @SerialName("CharHeightMetric") val height: String,
    @SerialName("Hobby") val hobbies: String,
    @SerialName("Designer") val designer: String,
    @SerialName("Illustrator") val illustrator: String,
    @SerialName("CharacterVoice") val cv: String,
    // img
    @SerialName("DevName") val devName: String,
    @SerialName("CollectionBG") val bgImgPath: String,
    @SerialName("WeaponImg") val weaponImgPath: String,
    )
