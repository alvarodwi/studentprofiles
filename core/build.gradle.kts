plugins {
    id("studentprofiles.android.library")
    id("studentprofiles.android.koin")
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "me.varoa.studentprofiles.core"

    defaultConfig {
        javaCompileOptions {
            annotationProcessorOptions {
                compilerArgumentProviders(
                    RoomSchemaArgProvider(File(project.rootDir, "schemas"))
                )
            }
        }
    }
}

dependencies {
    // coroutine
    api(libs.kotlinx.coroutines.android)

    // local
    implementation(libs.bundles.androidx.localPersistence)
    implementation(libs.okio)
    api(libs.androidx.datastore)
    kapt(libs.room.compiler)

    // remote
    implementation(libs.bundles.networking)

    // work
    api(libs.androidx.work)

    // other
    api(libs.androidx.paging)
    api(libs.coil)
    api(libs.logcat)

    // unit test
    api(libs.espresso.idlingResource)
    testImplementation(libs.bundles.unitTest)
}

class RoomSchemaArgProvider(
    @get:InputDirectory
    @get:PathSensitive(PathSensitivity.RELATIVE)
    val schemaDir: File
) : CommandLineArgumentProvider {

    override fun asArguments(): Iterable<String> {
        // Note: If you're using KSP, change the line below to return
        // listOf("room.schemaLocation=${schemaDir.path}").
        return listOf("-Aroom.schemaLocation=${schemaDir.path}")
    }
}
