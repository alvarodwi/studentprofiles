plugins {
    id("studentprofiles.android.feature")
}

android {
    namespace = "me.varoa.studentprofiles.feature.favorite"
    buildFeatures.viewBinding = true
}

dependencies {
    implementation(project(":app"))
    testImplementation(libs.bundles.unitTest)
}
