import com.dentmanage.gradle.LibraryPlugin

plugins {
    kotlin("jvm") version "2.1.21"
}

subprojects {
    apply<LibraryPlugin>()
}
dependencies {
    implementation(kotlin("stdlib-jdk8"))
}
repositories {
    mavenCentral()
}
kotlin {
    jvmToolchain(21)
}