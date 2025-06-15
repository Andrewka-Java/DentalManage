import com.dentmanage.gradle.ServicePlugin

repositories {
    maven { url = uri("https://plugins.gradle.org/m2/") }
}

dependencies {
//    runtimeOnly(libs.jib)
}

inline val Project.applicationConfig: JavaApplication get() = extensions.getByType<JavaApplication>()

val servicesJibDir: Directory = project.layout.projectDirectory.dir("jib")

subprojects {
//    apply<PreJibPlugin>()
//    apply<JibPlugin>()
    apply<ApplicationPlugin>()
    apply<ServicePlugin>()
    apply<JacocoPlugin>()

    val jacocoExecutionFile = "jacoco/coverage.exec"
    val jacocoXmlReportFile = "jacoco/jacocoTestReport.xml"
    
    val jacocoVerification = tasks.withType<JacocoCoverageVerification>()
    val jacocoEnabled = jacocoVerification.any { it.enabled }
    val jacocoReport = tasks.withType<JacocoReport> {
    onlyIf { jacocoEnabled }
        executionData.setFrom(layout.buildDirectory.file(jacocoExecutionFile))
    }
    
    tasks.named<Test>("test") {
        finalizedBy(jacocoReport)
        finalizedBy(jacocoVerification)
        configure<JacocoTaskExtension> {
        setDestinationFile(layout.buildDirectory.file(jacocoExecutionFile).map { it.asFile })
        }
    }
    
    afterEvaluate {
        tasks.withType<JacocoCoverageVerification> {
            onlyIf { jacocoEnabled }
            executionData.setFrom(layout.buildDirectory.file(jacocoExecutionFile))
            violationRules {
                rule {
                    limit {
                        val testCoverageThreshold: Any? by project.extra
                        minimum = testCoverageThreshold?.let { BigDecimal(it.toString()) ?: BigDecimal("0.5") }
                    }
                }
            }
        }
    }
}