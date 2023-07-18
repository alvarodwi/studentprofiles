package me.varoa.studentprofiles.core.util

import me.varoa.studentprofiles.core.data.remote.ApiConfig.IMAGE_URL
import me.varoa.studentprofiles.core.domain.model.Student

object ImageUtil {
    fun generateCollectionImageUrl(devName : String) =
        "${IMAGE_URL}student/collection/Student_Portrait_${devName}_Collection.webp"

    fun generatePortraitImageUrl(devName: String) =
        "${IMAGE_URL}student/portrait/Portrait_${devName}.webp"

    fun generateBackgroundImageUrl(bgImgPath: String) =
        "${IMAGE_URL}background/${bgImgPath}.jpg"

    fun generateWeaponImageUrl(weaponImgPath: String) =
        "${IMAGE_URL}weapon/${weaponImgPath}.jpg"
}
