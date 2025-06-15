pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
    includeBuild("build-logic")
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.5.0"
}

rootProject.name = "backend"

fun isProject(file: File): Boolean {
    return file.isDirectory && File(file, "build.gradle.kts").exists()
}

fun discoverProjects(rootDir: File, vararg roots: String): List<String> {
    val list = mutableListOf<String>()
    roots.forEach { root ->
        File(rootDir, root).listFiles()?.forEach { dir ->
            if (dir.isDirectory && isProject(dir)) {
                list.add(":$root:${dir.name}")
            }
        }
    }
    return list
}

// Include discovered projects from "services" directory
include(*discoverProjects(rootProject.projectDir, "services").toTypedArray())

// Example of manually including other projects if needed
// include("libraries:logger")
// include("libraries:auth")
// include("libraries:common")
// include("libraries:database")

// Optionally rename projects
// findProject(":libraries:database")?.name = "database"
