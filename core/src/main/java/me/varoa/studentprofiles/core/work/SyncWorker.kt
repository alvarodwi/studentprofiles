package me.varoa.studentprofiles.core.work

import android.content.Context
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import kotlinx.coroutines.delay
import kotlinx.serialization.json.Json
import logcat.logcat
import me.varoa.studentprofiles.core.data.remote.ApiConfig
import me.varoa.studentprofiles.core.data.remote.json.StudentJson
import me.varoa.studentprofiles.core.domain.model.SyncInterval
import me.varoa.studentprofiles.core.domain.usecase.SyncStudentUseCase
import me.varoa.studentprofiles.core.util.ImageUtil
import me.varoa.studentprofiles.core.util.LocalizationUtil
import me.varoa.studentprofiles.core.util.asEntity
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.internal.headersContentLength
import okio.buffer
import okio.sink
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.time.Duration
import java.util.Calendar
import java.util.Locale

class SyncWorker(
    appContext: Context,
    params: WorkerParameters,
    private val useCase: SyncStudentUseCase,
    private val client: OkHttpClient,
) : CoroutineWorker(appContext, params) {
    companion object {
        const val TAG = "sync_worker"
        const val PARAM_PROGRESS = "progress"
        const val KEY_MESSAGE = "message"

        fun scheduleNextWork(
            context: Context,
            interval: SyncInterval = SyncInterval.WEEKLY,
        ) {
            val constraints =
                Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build()
            val request =
                OneTimeWorkRequestBuilder<SyncWorker>()
                    .setConstraints(constraints)
                    .setInitialDelay(nextSyncDuration(interval))
                    .build()
            WorkManager.getInstance(context)
                .enqueueUniqueWork(TAG, ExistingWorkPolicy.REPLACE, request)
        }

        private fun nextSyncDuration(interval: SyncInterval): Duration {
            val calendar = Calendar.getInstance()
            val nowMillis = calendar.timeInMillis
            logcat { SimpleDateFormat("yyyy/MM/dd", Locale.getDefault()).format(calendar.time) }
            when (interval) {
                SyncInterval.WEEKLY -> calendar.add(Calendar.WEEK_OF_YEAR, 1)
                SyncInterval.BIWEEKLY -> calendar.add(Calendar.WEEK_OF_YEAR, 2)
                else -> calendar.add(Calendar.MONTH, 1)
            }
            logcat { SimpleDateFormat("yyyy/MM/dd", Locale.getDefault()).format(calendar.time) }
            val diff = calendar.timeInMillis - nowMillis
            logcat { diff.toString() }

            return Duration.ofMillis(diff)
        }
    }

    private val json =
        Json {
            ignoreUnknownKeys = true
            isLenient = true
        }

    override suspend fun doWork(): Result {
        try {
            // download json file
            setProgress(workDataOf(PARAM_PROGRESS to "Fetching students data..."))
            val request =
                Request.Builder()
                    .url(ApiConfig.STUDENT_JSON_URL)
                    .build()

            val response: Response?
            try {
                response = client.newCall(request).execute()
            } catch (e: IOException) {
                logcat { "Error while fetching students data : " + e.message }
                throw e
            }
            val jsonData = checkNotNull(response.body?.string()) { "JsonData is null" }
            val students = json.decodeFromString<List<StudentJson>>(jsonData)
            val totalData = students.size

            // save json data into database
            setProgress(workDataOf(PARAM_PROGRESS to "Saving data into the database..."))
            useCase.deleteAllStudent()
            students.map { it.asEntity() }.forEach { entity ->
                useCase.insertStudent(entity)
            }
            // preparing image folders
            val portraitDir = getDirectory("portrait")
            val collectionDir = getDirectory("collection")
            val bgDir = getDirectory("bg")
            val weaponDir = getDirectory("weapon")
            // try to download students images
            students.forEachIndexed { index, student ->
                setProgress(workDataOf(PARAM_PROGRESS to "Downloading student's image... (${index + 1}/$totalData)"))
                // download portraitImage
                with(student.devName) {
                    val file = File(portraitDir, "portrait_${student.id}.webp")
                    if (!file.exists()) {
                        val fileRequest = buildRequest(ImageUtil.generatePortraitImageUrl(this))
                        downloadFile(file, fileRequest, client)
                    } else {
                        logcat { "Image ${file.name} already exist! Skipping..." }
                    }
                }
                // download collectionImage
                with(student.imgPath) {
                    val file = File(collectionDir, "collection_${student.id}.webp")
                    if (!file.exists()) {
                        val fileRequest = buildRequest(ImageUtil.generateCollectionImageUrl(this))
                        downloadFile(file, fileRequest, client)
                    } else {
                        logcat { "Image ${file.name} already exist! Skipping..." }
                    }
                }
                // download weaponImage
                with(student.weaponImgPath) {
                    val file = File(weaponDir, "weapon_${student.id}.png")
                    if (!file.exists()) {
                        val fileRequest = buildRequest(ImageUtil.generateWeaponImageUrl(this))
                        downloadFile(file, fileRequest, client)
                    } else {
                        logcat { "Image ${file.name} already exist! Skipping..." }
                    }
                }
            }
            // download bgImgPath
            setProgress(workDataOf(PARAM_PROGRESS to "Downloading student's background image..."))
            students.map { it.bgImgPath }.distinct().forEach { bgPath ->
                val file = File(bgDir, "$bgPath.jpg")
                if (!file.exists()) {
                    val fileRequest = buildRequest(ImageUtil.generateBackgroundImageUrl(bgPath))
                    downloadFile(file, fileRequest, client)
                } else {
                    logcat { "Image ${file.name} already exist! Skipping..." }
                }
            }

            setProgress(workDataOf(PARAM_PROGRESS to "Finalizing sync..."))
            delay(1000L)
            scheduleNextWork(applicationContext, useCase.getSyncInterval())
            return Result.success(workDataOf(KEY_MESSAGE to "Synced $totalData students data"))
        } catch (ex: IllegalStateException) {
            return Result.failure(workDataOf(KEY_MESSAGE to ex.message))
        } catch (ex: IOException) {
            return Result.failure(workDataOf(KEY_MESSAGE to ex.message))
        }
    }

    private fun buildRequest(url: String) =
        Request.Builder()
            .url(url)
            .build()

    private fun getDirectory(dirName: String): File =
        File(applicationContext.filesDir, dirName)
            .also {
                if (!it.exists()) it.mkdir()
            }

    private fun downloadFile(
        file: File,
        request: Request,
        client: OkHttpClient,
    ) {
        file.createNewFile()
        try {
            logcat { "Starting download for file ${file.name}" }
            val response = client.newCall(request).execute()
            if (response.isSuccessful) {
                file.sink().buffer().use { sink ->
                    sink.write(response.body?.byteString() ?: throw IOException("Failed to write file from response : $response"))
                    sink.close()
                }
                logcat { "Download finished with size ${LocalizationUtil.humanReadableByteCount(response.headersContentLength())}" }
            } else {
                logcat { "Error ${response.code} : ${response.message}" }
            }
        } catch (ex: IOException) {
            ex.printStackTrace()
        } catch (ex: IllegalStateException) {
            ex.printStackTrace()
        }
    }
}
