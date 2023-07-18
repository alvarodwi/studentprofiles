plugins {
    id("studentprofiles.android.library")
    id("studentprofiles.android.hilt")
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
    kapt(libs.room.compiler)

    // remote
    implementation(libs.bundles.networking)

    // other
    api(libs.androidx.work)
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
