buildscript {
    apply(from = rootProject.file("../versions.gradle"))
}

plugins {
    kotlin("jvm") version "1.9.24"
    `java-gradle-plugin`
}

repositories {
    mavenCentral()
    gradlePluginPortal()
}

// tasks.register<Test>("unitTest") {
//     group = "messenger"
//     description = "Run unit tests"
//     reports.junitXml.destination = file("${rootProject.buildDir}/test-reports/${project.name}-unit")
// }

dependencies {
    implementation("org.apache.groovy:groovy:4.0.2")
}
