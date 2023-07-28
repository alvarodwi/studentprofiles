package me.varoa.studentprofiles.core.util

import me.varoa.studentprofiles.core.data.remote.ApiConfig.IMAGE_URL

object ImageUtil {
    fun generateCollectionImageUrl(imgPath: String) = "${IMAGE_URL}student/collection/$imgPath.webp"

    fun generatePortraitImageUrl(devName: String) = "${IMAGE_URL}student/portrait/Portrait_$devName.webp"

    fun generateBackgroundImageUrl(bgImgPath: String) = "${IMAGE_URL}background/$bgImgPath.jpg"

    fun generateWeaponImageUrl(weaponImgPath: String) = "${IMAGE_URL}weapon/$weaponImgPath.png"
}
