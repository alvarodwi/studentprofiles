plugins {
    id("studentprofiles.android.library")
    id("studentprofiles.android.hilt")
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "me.varoa.studentprofiles.core"
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
    implementation(libs.jsoup)
    api(libs.androidx.work)
    api(libs.androidx.paging)
    api(libs.coil)
    api(libs.logcat)

    // unit test
    api(libs.espresso.idlingResource)
    testImplementation(libs.bundles.unitTest)
}
