package me.varoa.studentprofiles.core.domain.model

enum class School(val key: String, val fullName: String) {
    ABYDOS("Abydos", "Abydos High School"),
    ARIUS("Arius", "Arius Satellite School"),
    GEHENNA("Gehenna", "Gehenna Academy"),
    HYAKKIYAKO("Hyakkiyako", "Allied Hyakkiyako Academy"),
    MILLENIUM("Millennium", "Millennium Science School"),
    RED_WINTER("RedWinter", "Red Winter Federal Academy"),
    SHANHAIJING("Shanhaijing", "Shanhaijing Academy"),
    SRT("SRT", "SRT Special Academy"),
    TRINITY("Trinity", "Trinity General School"),
    VALKYRIE("Valkyrie", "Valkyrie Police School"),
    ETC("ETC", "Etc."),
    ;

    companion object {
        private val MAP: Map<String, School> = School.values().associateBy { it.key }

        infix fun from(key: String) = MAP[key] ?: ETC
    }
}
