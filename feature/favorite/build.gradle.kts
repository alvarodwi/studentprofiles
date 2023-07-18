plugins {
    id("studentprofiles.android.feature")
    id("studentprofiles.android.hilt")
}

android {
    namespace = "me.varoa.studentprofiles.feature.favorite"
    buildFeatures.viewBinding = true
}

dependencies {
    testImplementation(libs.bundles.unitTest)
}
