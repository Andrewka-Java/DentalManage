plugins {
    alias(libs.plugins.kotlin.jvm)
    id("java-gradle-plugin")
}

repositories {
    mavenCentral()
    gradlePluginPortal()
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
        vendor.set(JvmVendorSpec.AMAZON)
    }
}

tasks.named<Test>("test") {
    group = "DentManage"
    description = "Run unit tests"
    useJUnitPlatform()
}

dependencies {
   implementation(libs.bundles.http4k.json)
   implementation(libs.bundles.http4k.client)
   implementation(libs.postgresql)

   testImplementation(libs.bundles.testing.core)
}

gradlePlugin {
    plugins.register("gradle-settings") {
        id = "gradle-settings"
        implementationClass = "com.dentmanage.gradle.GradleSettingsPlugin"
    }

//    todo: pre Jib
}