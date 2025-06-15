
dependencies {
//    api(libs.connect.commons.lang)
//    api(libs.connect.commons.json)
    api(libs.bundles.http4k.core)
    implementation(libs.bundles.logback)
    implementation(libs.nimbus.jwt)
    implementation(libs.kotlinx.coroutines.core)

    testImplementation(libs.mockk)

    testImplementation(libs.mockk)

//    testFixturesApi(testFixtures(libs.connect.commons.lang))
//    testFixturesApi(testFixtures(project(":libraries:openapi")))
    testFixturesImplementation(libs.bundles.http4k.core)
    testFixturesImplementation(libs.nimbus.jwt)
}