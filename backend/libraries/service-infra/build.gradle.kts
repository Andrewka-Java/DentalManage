dependencies {
    api(project(":libraries:common"))
    api(libs.bundles.http4k.core)
    implementation(libs.bundles.http4k.client)
    testImplementation(libs.mockk)

    testImplementation(testFixtures(project(":libraries:common")))

    testFixturesApi(testFixtures(project(":libraries:common")))
//    testFixturesApi(project(":libraries:test-docker-support"))
    testFixturesApi(libs.kotlinx.coroutines.test)
}