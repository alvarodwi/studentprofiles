plugins {
    alias(libs.plugins.versions)
    alias(libs.plugins.kotlinter)
    alias(libs.plugins.detekt)
    alias(libs.plugins.vc.update)

    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.gradle.secrets) apply false
    alias(libs.plugins.androidx.navigation.safeArgs) apply false
    id("com.android.dynamic-feature") version "8.0.2" apply false
}

allprojects {
    // apply ktlint
    apply(plugin = "org.jmailen.kotlinter")

    // Ktlint configuration for sub-projects
    kotlinter {
        ignoreFailures = false
        reporters = arrayOf("checkstyle", "plain")
    }
}

subprojects {
    apply(plugin = "io.gitlab.arturbosch.detekt")

    detekt {
        config.setFrom("${project.rootDir}/detekt.yml")
        buildUponDefaultConfig = true
        parallel = true
    }
}



// ben-manes versions checking
fun isNonStable(version: String): Boolean {
    val stableKeyword = listOf("RELEASE", "FINAL", "GA").any {
        version.uppercase()
            .contains(it)
    }
    val regex = "^[0-9,.v-]+(-r)?$".toRegex()
    val isStable = stableKeyword || regex.matches(version)
    return isStable.not()
}

tasks.withType<com.github.benmanes.gradle.versions.updates.DependencyUpdatesTask> {
    // reject all non stable versions
    rejectVersionIf {
        isNonStable(candidate.version)
    }

    // optional parameters
    checkForGradleUpdate = true
    outputFormatter = "json"
    outputDir = "build/dependencyUpdates"
    reportfileName = "report"
}

versionCatalogUpdate {
    sortByKey.set(true)
    keep {
        // keep versions without any library or plugin reference
        keepUnusedVersions.set(true)
        // keep all libraries that aren't used in the project
        keepUnusedLibraries.set(true)
        // keep all plugins that aren't used in the project
        keepUnusedPlugins.set(true)
    }
}

