plugins {
    id("studentprofiles.android.application")
    id("studentprofiles.android.hilt")
    id("androidx.navigation.safeargs.kotlin")
}

android {
    namespace = "me.varoa.studentprofiles"
    defaultConfig {
        applicationId = "me.varoa.studentprofiles"
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android.txt"),
                "proguard-rules.pro"
            )
        }
        debug {
            applicationIdSuffix = ".debug"
            isDebuggable = true
        }
    }
    buildFeatures.viewBinding = true
    dynamicFeatures.add(":feature:favorite")
}

dependencies {
    // core module
    api(project(":core"))

    // androidx
    api(libs.androidx.core)
    api(libs.androidx.appcompat)
    api(libs.bundles.androidx.navigation)
    api(libs.bundles.androidx.lifecycle)

    // ui
    implementation(libs.google.material)
    implementation(libs.androidx.constraintLayout)
    implementation(libs.androidx.swipeRefreshLayout)
    implementation(libs.androidx.viewpager2)
    implementation(libs.androidx.preference)

    // other
    implementation(libs.hilt.ext.navigation)

    // testing
    testImplementation(libs.bundles.unitTest)
    androidTestImplementation(libs.bundles.uiTest)
    androidTestImplementation(libs.androidx.navigation.testing)
    androidTestImplementation(libs.hilt.android.testing)
    kaptAndroidTest(libs.hilt.compiler)
}
