package me.varoa.studentprofiles.core

import me.varoa.studentprofiles.core.domain.model.AttackType.Explosive
import me.varoa.studentprofiles.core.domain.model.DefenseType.Light
import me.varoa.studentprofiles.core.domain.model.School.GEHENNA
import me.varoa.studentprofiles.core.domain.model.SquadType.Striker
import me.varoa.studentprofiles.core.domain.model.Student
import me.varoa.studentprofiles.core.domain.model.StudentMinified
import me.varoa.studentprofiles.core.domain.model.StudentPosition.Front
import me.varoa.studentprofiles.core.domain.model.StudentProfile
import me.varoa.studentprofiles.core.domain.model.TacticRole.Dealer
import me.varoa.studentprofiles.core.domain.model.WeaponType.AR

object DummyHelper {
    // i don't want to give default value to domain model, so here we are...
    fun generateTestStudent(id: Int): Student =
        Student(
            id = id,
            name = "Student_$id",
            releasedInGlobal = true,
            // the rest is not that important in test...
            imgPath = "",
            squadType = Striker,
            tacticRole = Dealer,
            attackType = Explosive,
            defenseType = Light,
            weaponType = AR,
            position = Front,
            school = GEHENNA,
            profile = StudentProfile("", "", "", "", "", "", "", "", "", "", "", "", "", ""),
            isFavorite = false,
        )

    fun generateTestStudentMinified(id: Int) =
        StudentMinified(
            id = id,
            name = "Student_$id",
            imgPath = "",
            squadType = "",
            tacticRole = "",
        )
}
