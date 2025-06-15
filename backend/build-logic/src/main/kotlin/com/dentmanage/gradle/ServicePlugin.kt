package com.dentmanage.gradle

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.provider.SetProperty
import org.gradle.api.model.ObjectFactory
import org.gradle.api.tasks.testing.Test
import javax.inject.Inject

open class ServiceExtension @Inject constructor(objects: ObjectFactory) {
    var parallelTestsEnabled: Boolean = false
    val parallelTestsExclusions: SetProperty<String> = objects.setProperty(String::class.java).convention(emptySet())

    companion object {
        internal const val extensionName = "dentalService"
    }
}

class ServicePlugin : Plugin<Project> {
    override fun apply(project: Project) {
        val extension = project.extensions.create(ServiceExtension.extensionName, ServiceExtension::class.java)

        project.dependencies.apply {
            add("implementation", project.project(":libraries:service-infra"))
            add("testImplementation", testFixtures(project.project(":libraries:service-infra")))
            add("testFixturesImplementation", testFixtures(project.project(":libraries:service-infra")))
        }

        // wait until the project has been fully configured before the _extension_ will be fully configured
        project.afterEvaluate {
            if (project.subprojects.isNotEmpty()) {
                throw IllegalStateException(
                    "Service project ${project.path} has subprojects: ${project.subprojects.joinToString { it.name }} -- this is not allowed at the moment, to simplify independent pipelines"
                )
            }

            project.localBuild { env ->
                dependsOn("serviceBuild")
            }

            // tests
            project.registerTask("serviceBuild") {
                dependsOn("buildContainer")
            }

            project.registerTask("buildContainer") {
                dependsOn(
                    "ktlintCheck",
                    "test"
                )
            }


        }
    }

    private fun Project.configureParallelTests(extension: ServiceExtension) {
        tasks.register("testParallel", Test::class.java) { task ->
            task.onlyIf { extension.parallelTestsEnabled }
            task.group = "dentalmanage"
            task.description = "Runs specified tests in parallel"
            task.reports.junitXml.outputLocation.set(project.layout.buildDirectory.dir("test-reports/${project.name}-testParallel"))
            task.useJUnitPlatform()

            task.maxParallelForks = Runtime.getRuntime().availableProcessors().coerceAtMost(8)
            task.jvmArgs("-Xmx2048", "-XX:+HeapDumpOnOutOfMemoryError", "-XX:HeapDumpPath=build/reports/tests/")
            task.doFirst {
                task.exclude(extension.parallelTestsExclusions.get())
            }
        }

        tasks.register("testSequential", Test::class.java) { task ->
            task.onlyIf { extension.parallelTestsEnabled }
            task.group = "dentalmanage"
            task.description = "Runs specified tests in sequuentially"
            task.reports.junitXml.outputLocation.set(project.layout.buildDirectory.dir("test-reports/${project.name}-testSequential"))
            task.useJUnitPlatform()
            task.maxParallelForks = 1

            task.maxParallelForks = Runtime.getRuntime().availableProcessors().coerceAtMost(8)
            task.jvmArgs("-Xmx2048", "-XX:+HeapDumpOnOutOfMemoryError", "-XX:HeapDumpPath=build/reports/tests/")
            task.doFirst {
                if (extension.parallelTestsExclusions.get().isNotEmpty()) {
                    task.include(extension.parallelTestsExclusions.get())
                }
            }
        }
    }
}