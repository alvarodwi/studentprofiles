package me.varoa.studentprofiles.core.util

object LocalizationUtil {
    // from https://schale.gg/data/en/localization.min.json
    // hardcoded for my sanity (sorry)
    fun fetchClubName(name: String): String {
        return when (name) {
            "Kohshinjo68" -> "Problem Solver 68"
            "Justice" -> "Justice Task Force"
            "CleanNClearing" -> "Cleaning & Clearing"
            "BookClub" -> "Library Committee"
            "Countermeasure" -> "Foreclosure Task Force"
            "Engineer" -> "Engineering Department"
            "FoodService" -> "School Lunch Club"
            "Fuuki" -> "Prefect Team"
            "GourmetClub" -> "Gourmet Research Society"
            "HoukagoDessert" -> "After-School Sweets Club"
            "KnightsHospitaller" -> "Remedial Knights"
            "MatsuriOffice" -> "Festival Operations Department"
            "Meihuayuan" -> "Plum Blossom Garden"
            "Onmyobu" -> "Yin-Yang Club"
            "RemedialClass" -> "Make-Up Work Club"
            "SPTF" -> "Super Phenomenon Task Force"
            "Shugyobu" -> "Inner Discipline Club"
            "Endanbou" -> "Eastern Alchemy Society"
            "TheSeminar" -> "Seminar"
            "TrainingClub" -> "Athletics Training Club"
            "TrinityVigilance" -> "Trinity's Vigilante Crew"
            "Veritas" -> "Veritas"
            "NinpoKenkyubu" -> "Ninjutsu Research Club"
            "GameDev" -> "Game Development Department"
            "RedwinterSecretary" -> "Red Winter Office"
            "anzenkyoku" -> "Public Safety Bureau"
            "SisterHood" -> "The Sisterhood"
            "Class227" -> "Spec Ops No. 227"
            "Emergentology" -> "Medical Emergency Club"
            "RabbitPlatoon" -> "RABBIT Squad"
            "PandemoniumSociety" -> "Pandemonium Society"
            "AriusSqud" -> "Arius Squad"
            "HotSpringsDepartment" -> "Hot Springs Department"
            "TeaParty" -> "Tea Party"
            "PublicPeaceBureau" -> "Public Peace Bureau"
            "BlackTortoisePromenade" -> "Black Tortoise Promenade"
            "Genryumon" -> "Genryumon"
            "LaborParty" -> "Labor Party"
            else -> "None"
        }
    }
}
