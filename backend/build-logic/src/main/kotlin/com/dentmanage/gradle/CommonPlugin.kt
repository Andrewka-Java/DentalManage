package com.dentmanage.gradle

import com.dentmanage.gradle.LocalEnvironment.Companion.onLocal
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.tasks.TaskProvider
import org.gradle.api.tasks.testing.Test

class CommonPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        project.configureTests()
    }

    private fun Project.configureTests() {
        val test = tasks.named("test", Test::class.java) { task ->
            task.group = "dentmanage"
            task.description = "Run unit tests against local environment"
            task.reports.junitXml.outputLocation.set(project.layout.buildDirectory.dir("test-reporters/${project.name}-unit"))
            task.useJUnitPlatform()
            task.jvmArgs("-Xmx2048m", "-XX:+HeapDumpOnOutOfMemoryError", "-XX:HeapDumpPath=build/reports/tests/")
        }

        // todo: here database config
    }
}

fun Project.localBuild(configure: Task.(env: LocalEnvironment) -> Unit) = project.registerTask("localBuild") {
    onLocal {
        configure(this)
    }
}

fun Project.registerTask(name: String, configure: Task.() -> Unit): TaskProvider<Task> {
    return project.tasks.register(name) {
        it.group = "dentmanage"
        configure(it)
    }
}
@JvmName("registerTaskWithType")
inline fun <reified T : Task> Project.registerTask(name: String, crossinline configure: Task.() -> Unit): TaskProvider<Task> {
    return project.tasks.register(name) {
        it.group = "dentmanage"
        configure(it)
    }
}