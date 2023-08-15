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

    fun humanReadableByteCount(bytes: Long) =
        when {
            bytes == Long.MIN_VALUE || bytes < 0 -> "N/A"
            bytes < 1024L -> "$bytes B"
            bytes <= 0xfffccccccccccccL shr 40 -> "%.1f KiB".format(bytes.toDouble() / (0x1 shl 10))
            bytes <= 0xfffccccccccccccL shr 30 -> "%.1f MiB".format(bytes.toDouble() / (0x1 shl 20))
            bytes <= 0xfffccccccccccccL shr 20 -> "%.1f GiB".format(bytes.toDouble() / (0x1 shl 30))
            bytes <= 0xfffccccccccccccL shr 10 -> "%.1f TiB".format(bytes.toDouble() / (0x1 shl 40))
            bytes <= 0xfffccccccccccccL -> "%.1f PiB".format((bytes shr 10).toDouble() / (0x1 shl 40))
            else -> "%.1f EiB".format((bytes shr 20).toDouble() / (0x1 shl 40))
        }
}
