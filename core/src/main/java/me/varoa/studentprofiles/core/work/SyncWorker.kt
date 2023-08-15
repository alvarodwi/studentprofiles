package me.varoa.studentprofiles.core.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import kotlinx.serialization.json.Json
import logcat.logcat
import me.varoa.studentprofiles.core.data.remote.ApiConfig
import me.varoa.studentprofiles.core.data.remote.json.StudentJson
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

class SyncWorker(
    appContext: Context,
    params: WorkerParameters,
    private val useCase: SyncStudentUseCase,
    private val client: OkHttpClient,
) : CoroutineWorker(appContext, params) {
    private val json =
        Json {
            ignoreUnknownKeys = true
            isLenient = true
        }

    override suspend fun doWork(): Result {
        try {
            // download json file
            logcat("SyncWorker") { "Download json file" }
            val request =
                Request.Builder()
                    .url(ApiConfig.STUDENT_JSON_URL)
                    .build()

            val response: Response?
            try {
                response = client.newCall(request).execute()
            } catch (e: IOException) {
                logcat { "Error while fetching students data : " + e.message }
                return Result.failure()
            }
            val jsonData = checkNotNull(response.body?.string()) { "JsonData is null" }
            val students = json.decodeFromString<List<StudentJson>>(jsonData)
            logcat("SyncWorker") { "Preview of one student : ${students[0]}" }

            // save json data into database
            logcat("SyncWorker") { "Save json data into database" }
            useCase.deleteAllStudent()
            students.map { it.asEntity() }.forEach { entity ->
                useCase.insertStudent(entity)
            }

            // try to download students images
            val portraitDir = getDirectory("portrait")
            val collectionDir = getDirectory("collection")
            val bgDir = getDirectory("bg")
            val weaponDir = getDirectory("weapon")
            students.forEach { student ->
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
            students.map { it.bgImgPath }.distinct().forEach { bgPath ->
                val file = File(bgDir, "$bgPath.jpg")
                if (!file.exists()) {
                    val fileRequest = buildRequest(ImageUtil.generateBackgroundImageUrl(bgPath))
                    downloadFile(file, fileRequest, client)
                } else {
                    logcat { "Image ${file.name} already exist! Skipping..." }
                }
            }

            return Result.success()
        } catch (e: IllegalStateException) {
            logcat { "IllegalState : " + e.message }
            return Result.failure()
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
