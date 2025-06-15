// Apply versions.gradle using Kotlin DSL
apply(from = rootProject.file("versions.gradle"))

plugins {
    id("org.jetbrains.kotlin.jvm") version kotlinVersion
}

subprojects {
    apply(from = rootProject.file("versions.gradle"))
    apply(plugin = "org.jetbrains.kotlin.jvm")
}

allprojects {
    group = "com.messenger"
    version = "1.0-SNAPSHOT"

    repositories {
        mavenCentral()
    }
}
