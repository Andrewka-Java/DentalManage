package com.dentmanage.gradle

import org.gradle.api.Plugin
import org.gradle.api.initialization.Settings
import java.io.File
import java.io.FileFilter

class GradleSettingsPlugin : Plugin<Settings> {
    override fun apply(settings: Settings) {
        val rootProject = settings.rootProject

        settings.include(rootProject.projectDir.discoverProjects())
        settings.buildCache { cache ->
            cache.local {
                it.directory = "${rootProject.projectDir}/.gradle/local-build-cache"
            }
        }
    }
}

private fun File.discoverProjects(): List<String> = this
    .listFiles(FileFilter { it.isProject() })
    .orEmpty()
    .map { it.name }
    .filter { name -> name != "build-logic" }
    .flatMap { root ->
        File(this, root)
            .listFiles(FileFilter { it.isProject() })
            .orEmpty()
            .map { dir ->
                ":$root:${dir.name}"
            }
    }

fun File.isProject() =
    isDirectory() && (File(this, "build.gradle").exists() || File(this, "build.gradle.kts").exists())