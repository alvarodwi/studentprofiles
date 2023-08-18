package me.varoa.studentprofiles.core.util

object LocalizationUtil {
    // from https://schale.gg/data/en/localization.min.json
    // hardcoded for my sanity (sorry)
    private val clubNames =
        listOf(
            "Kohshinjo68" to "Problem Solver 68",
            "Justice" to "Justice Task Force",
            "CleanNClearing" to "Cleaning & Clearing",
            "BookClub" to "Library Committee",
            "Countermeasure" to "Foreclosure Task Force",
            "Engineer" to "Engineering Department",
            "FoodService" to "School Lunch Club",
            "Fuuki" to "Prefect Team",
            "GourmetClub" to "Gourmet Research Society",
            "HoukagoDessert" to "After-School Sweets Club",
            "KnightsHospitaller" to "Remedial Knights",
            "MatsuriOffice" to "Festival Operations Department",
            "Meihuayuan" to "Plum Blossom Garden",
            "Onmyobu" to "Yin-Yang Club",
            "RemedialClass" to "Make-Up Work Club",
            "SPTF" to "Super Phenomenon Task Force",
            "Shugyobu" to "Inner Discipline Club",
            "Endanbou" to "Eastern Alchemy Society",
            "TheSeminar" to "Seminar",
            "TrainingClub" to "Athletics Training Club",
            "TrinityVigilance" to "Trinity's Vigilante Crew",
            "Veritas" to "Veritas",
            "NinpoKenkyubu" to "Ninjutsu Research Club",
            "GameDev" to "Game Development Department",
            "RedwinterSecretary" to "Red Winter Office",
            "anzenkyoku" to "Public Safety Bureau",
            "SisterHood" to "The Sisterhood",
            "Class227" to "Spec Ops No. 227",
            "Emergentology" to "Medical Emergency Club",
            "RabbitPlatoon" to "RABBIT Squad",
            "PandemoniumSociety" to "Pandemonium Society",
            "AriusSqud" to "Arius Squad",
            "HotSpringsDepartment" to "Hot Springs Department",
            "TeaParty" to "Tea Party",
            "PublicPeaceBureau" to "Public Peace Bureau",
            "BlackTortoisePromenade" to "Black Tortoise Promenade",
            "Genryumon" to "Genryumon",
            "LaborParty" to "Labor Party",
        )

    fun fetchClubCode(name: String): String = clubNames.find { it.second == name }?.first ?: "EmptyClub"

    fun fetchClubName(code: String): String = clubNames.find { it.first == code }?.second ?: "None"
}
