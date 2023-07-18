package me.varoa.studentprofiles.utils

import android.content.Context
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import me.varoa.studentprofiles.core.work.SyncWorker

object WorkUtil {
    fun startSyncWork(context : Context) {
        val syncRequest = OneTimeWorkRequestBuilder<SyncWorker>()
            .build()

        WorkManager.getInstance(context).enqueue(syncRequest)
    }
}
