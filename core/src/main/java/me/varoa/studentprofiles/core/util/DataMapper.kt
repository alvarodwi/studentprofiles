package me.varoa.studentprofiles.core.util

import me.varoa.studentprofiles.core.data.local.entity.StudentEntity
import me.varoa.studentprofiles.core.data.remote.json.StudentJson
import me.varoa.studentprofiles.core.domain.model.AttackType
import me.varoa.studentprofiles.core.domain.model.DefenseType
import me.varoa.studentprofiles.core.domain.model.School
import me.varoa.studentprofiles.core.domain.model.SquadType
import me.varoa.studentprofiles.core.domain.model.Student
import me.varoa.studentprofiles.core.domain.model.StudentPosition
import me.varoa.studentprofiles.core.domain.model.StudentProfile
import me.varoa.studentprofiles.core.domain.model.TacticRole
import me.varoa.studentprofiles.core.domain.model.WeaponType

fun StudentJson.asEntity() =
    StudentEntity(
        id = id,
        releasedInGlobal = isReleased[1],
        name = name,
        squadType = squadType,
        tacticRole = tacticRole,
        attackType = attackType,
        defenseType = defenseType,
        weaponType = weaponType,
        position = position,
        school = school,
        fullName = "$familyName $personalName",
        club = club,
        schoolYear = schoolYear,
        basicInfo = basicInfo,
        age = age,
        birthday = birthday,
        height = height,
        hobbies = hobbies,
        designer = designer,
        illustrator = illustrator,
        cv = cv,
        imgPath = imgPath,
        devName = devName,
        bgImgPath = bgImgPath,
        weaponImgPath = weaponImgPath,
    )

fun StudentEntity.asModel() =
    Student(
        id = id,
        releasedInGlobal = releasedInGlobal,
        name = name,
        squadType = SquadType from squadType,
        tacticRole = TacticRole from tacticRole,
        attackType = AttackType from attackType,
        defenseType = DefenseType from defenseType,
        weaponType = WeaponType.valueOf(weaponType),
        position = StudentPosition.valueOf(position),
        school = School from school,
        imgPath = imgPath,
        profile =
            StudentProfile(
                fullName = fullName,
                club = LocalizationUtil.fetchClubName(club),
                schoolYear = schoolYear,
                basicInfo = basicInfo,
                age = age,
                birthday = birthday,
                height = height,
                hobbies = hobbies,
                designer = designer,
                illustrator = illustrator,
                cv = cv,
                devName = devName,
                bgImgPath = bgImgPath,
                weaponImgPath = weaponImgPath,
            ),
        isFavorite = isFavorite,
    )
