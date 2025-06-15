configure<JavaApplication> {
    mainClass.set("com.dentmanage.user.UserMainKt")
}

dependencies {
    implementation(libs.bundles.http4k.client)
    testImplementation(libs.mockk)
    testImplementation(libs.bundles.testing.core)
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll("-Xjsr305=strict")
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
