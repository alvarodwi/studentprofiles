plugins {
    id("studentprofiles.android.feature")
    id("studentprofiles.android.koin")
}

android {
    namespace = "me.varoa.studentprofiles.feature.favorite"
    buildFeatures.viewBinding = true
}

dependencies {
    implementation(project(":core"))

    implementation(libs.google.material)
    implementation(libs.androidx.constraintLayout)

    testImplementation(libs.bundles.unitTest)
}
