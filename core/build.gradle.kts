plugins {
    id("studentprofiles.android.library")
    id("studentprofiles.android.koin")
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "me.varoa.studentprofiles.core"

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android.txt"),
                "proguard-rules.pro"
            )
            consumerProguardFiles("consumer-rules.pro")
        }
    }

    defaultConfig {
        testInstrumentationRunner  = "androidx.test.runner.AndroidJUnitRunner"

        ksp {
            arg("room.schemaLocation", "$projectDir/schemas")
        }
    }

    testOptions {
        unitTests.isReturnDefaultValues = true
    }

    packaging {
        resources.excludes.add("META-INF/*")
    }
}

dependencies {
    // coroutine
    api(libs.kotlinx.coroutines.android)

    // local
    implementation(libs.bundles.androidx.localPersistence)
    implementation(libs.okio)
    api(libs.androidx.datastore)
    ksp(libs.room.compiler)

    // remote
    implementation(libs.bundles.networking)

    // work
    api(libs.androidx.work)

    // other
    api(libs.androidx.paging)
    api(libs.coil)
    api(libs.logcat)

    // unit test
    androidTestImplementation(libs.bundles.uiTest)
    testImplementation(libs.bundles.unitTest)

    // testing database
    androidTestImplementation(libs.turbine)
    testImplementation(libs.room.testing)
}
