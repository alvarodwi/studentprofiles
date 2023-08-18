plugins {
    id("studentprofiles.android.application")
    id("studentprofiles.android.koin")
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
            isShrinkResources = true
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

    packaging {
        resources.excludes.add("META-INF/*")
    }
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
    implementation(libs.androidx.viewpager2)
    implementation(libs.androidx.preference)

    // testing
    testImplementation(libs.bundles.unitTest)
    androidTestImplementation(libs.bundles.uiTest)
    androidTestImplementation(libs.androidx.navigation.testing)

    // leakcanary
    debugImplementation(libs.leakcanary)
}
