package me.varoa.studentprofiles.ext

import android.content.Context
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import logcat.logcat
import me.varoa.studentprofiles.core.domain.model.Student
import me.varoa.studentprofiles.core.domain.model.StudentImageType
import me.varoa.studentprofiles.core.domain.model.StudentImageType.BG
import me.varoa.studentprofiles.core.domain.model.StudentImageType.PORTRAIT
import me.varoa.studentprofiles.core.domain.model.StudentImageType.WEAPON
import me.varoa.studentprofiles.core.domain.model.StudentMinified
import me.varoa.studentprofiles.core.domain.model.SyncInterval
import me.varoa.studentprofiles.core.work.SyncWorker
import java.io.File
import java.time.Duration
import java.util.Calendar

fun getImageDirectory(
    context: Context,
    dirName: String,
) = File(context.filesDir, dirName)

fun Student.getImage(
    context: Context,
    type: StudentImageType,
): File {
    return when (type) {
        BG -> File(getImageDirectory(context, "bg"), "${this.profile.bgImgPath}.jpg")
        WEAPON -> File(getImageDirectory(context, "weapon"), "weapon_${this.id}.png")
        PORTRAIT -> File(getImageDirectory(context, "portrait"), "portrait_${this.id}.webp")
        else -> File(getImageDirectory(context, "collection"), "collection_${this.id}.webp")
    }
}

fun StudentMinified.getImage(context: Context): File = File(getImageDirectory(context, "collection"), "collection_${this.id}.webp")
