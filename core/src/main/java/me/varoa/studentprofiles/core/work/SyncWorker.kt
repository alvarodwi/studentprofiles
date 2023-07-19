package me.varoa.studentprofiles.core.work

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.serialization.json.Json
import logcat.logcat
import me.varoa.studentprofiles.core.data.remote.ApiConfig
import me.varoa.studentprofiles.core.data.remote.json.StudentJson
import me.varoa.studentprofiles.core.domain.usecase.SyncStudentUseCase
import me.varoa.studentprofiles.core.util.asEntity
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

@HiltWorker
class SyncWorker
    @AssistedInject
    constructor(
        @Assisted appContext: Context,
        @Assisted params: WorkerParameters,
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

                var response: Response? = null
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

                return Result.success()
            } catch (e: IllegalStateException) {
                logcat { "IllegalState : " + e.message }
                return Result.failure()
            }
        }
    }
