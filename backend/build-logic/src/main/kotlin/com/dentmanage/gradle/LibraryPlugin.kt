package com.dentmanage.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project

class LibraryPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.localBuild {
            dependsOn(
                "ktlintCheck",
                "test"
            )
        }
    }
}