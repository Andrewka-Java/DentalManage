import com.dentmanage.gradle.CommonPlugin
import org.jlleitschuh.gradle.ktlint.KtlintPlugin
import org.jlleitschuh.gradle.ktlint.reporter.ReporterType.PLAIN

plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.ktlint)
}

allprojects {
    repositories {
        mavenCentral()
    }

    apply(plugin="kotlin")
    kotlin {
        jvmToolchain {
            languageVersion = JavaLanguageVersion.of(21)
            vendor = JvmVendorSpec.AMAZON
        }
    }

    apply<JavaTestFixturesPlugin>()
    apply<CommonPlugin>()

    apply<KtlintPlugin>()
    ktlint {
        version = provider { libs.versions.ktlint.get() }
        android = false
        verbose = true
        outputToConsole = true
        enableExperimentalRules = false
        reporters {
            reporter(PLAIN)
        }
        filter {
            exclude("**/generated/**")
            include("**/kotlin/**")
        }
    }

    group = "com.dentmanage." + project.path.replace(":", ".").removePrefix(".")
    version = "0.0.1"
}
