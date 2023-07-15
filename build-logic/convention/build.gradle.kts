plugins {
    `kotlin-dsl`
}

group = "me.varoa.studentprofiles.buildlogic"

// Configure the build-logic plugins to target JDK 17
// This matches the JDK used to build the project, and is not related to what is running on device.
java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}
tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
    }
}

dependencies {
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.kotlin.gradlePlugin)
}

gradlePlugin {
    plugins {
        register("androidApplication") {
            id = "studentprofiles.android.application"
            implementationClass = "AndroidApplicationConventionPlugin"
        }

        register("androidFeature") {
            id = "studentprofiles.android.feature"
            implementationClass = "AndroidFeatureConventionPlugin"
        }

        register("androidLibrary") {
            id = "studentprofiles.android.library"
            implementationClass = "AndroidLibraryConventionPlugin"
        }

        register("androidHilt") {
            id = "studentprofiles.android.hilt"
            implementationClass = "AndroidHiltConventionPlugin"
        }
    }
}
