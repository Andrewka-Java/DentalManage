extra.apply {
    val kotlinVersion = "1.9.25"
    val http4kVersion = "4.33.3.0"
    val jwtVersion = "0.9.1"
    val junitVersion = "5.9.0"
    val junitPlatformLauncherVersion = "1.9.0"
    val jsonUnitVersion = "2.21.0"
    val mockkVersion = "1.12.5"
    val jacksonVersion = "2.13.0"
    val jetbrainsExposedVersion = "0.33.1"
    val postgresVersion = "42.3.6"
    val kafkaVersion = "2.8.1"

    val jackson = listOf(
        "com.fasterxml.jackson.module:jackson-module-kotlin:$jacksonVersion",
        "com.fasterxml.jackson.datatype:jackson-datatype-jsr310:$jacksonVersion"
    )

    val kotlin = listOf(
        "org.jetbrains.kotlin:kotlin-stdlib-jdk8:$kotlinVersion",
        "org.jetbrains.kotlin:kotlin-reflect:$kotlinVersion"
    )

    val http4kJson = listOf(
        "org.http4k:http4k-format-jackson:$http4kVersion",
        "org.http4k:http4k-contract:$http4kVersion"
    )

    val http4k = listOf(
        "org.http4k:http4k-core:$http4kVersion",
        "org.http4k:http4k-server-undertow:$http4kVersion",
        "org.http4k:http4k-cloudnative:$http4kVersion",
        "org.http4k:http4k-opentelemetry:$http4kVersion"
    ) + http4kJson

    val http4k_client = listOf(
        "org.http4k:http4k-core:$http4kVersion",
        "org.http4k:http4k-cloudnative:$http4kVersion",
        "org.http4k:http4k-client-okhttp:$http4kVersion"
    ) + http4kJson

    val jwt = listOf(
        "io.jsonwebtoken:jjwt:$jwtVersion"
    )

    val database = listOf(
        "org.jetbrains.exposed:exposed-core:$jetbrainsExposedVersion",
        "org.jetbrains.exposed:exposed-dao:$jetbrainsExposedVersion",
        "org.jetbrains.exposed:exposed-jdbc:$jetbrainsExposedVersion",
        "org.jetbrains.exposed:exposed-java-time:$jetbrainsExposedVersion",
        "org.postgresql:postgresql:$postgresVersion"
    )

    val kafka = listOf(
        "org.apache.kafka:kafka-clients:$kafkaVersion",
        "org.apache.kafka:kafka-streams:$kafkaVersion"
    )

    val testingLibs = listOf(
        "org.junit.jupiter:junit-jupiter-api:$junitVersion",
        "org.junit.jupiter:junit-jupiter-engine:$junitVersion",
        "org.junit.jupiter:junit-jupiter-params:$junitVersion",
        "org.http4k:http4k-testing-approval:$http4kVersion",
        "org.http4k:http4k-testing-hamkrest:$http4kVersion",
        "org.junit.platform:junit-platform-launcher:$junitPlatformLauncherVersion"
    )

    val testingJsonUnit = listOf(
        "net.javacrumbs.json-unit:json-unit:$jsonUnitVersion"
    )

    val testingKafka = listOf(
        "org.apache.kafka:kafka-streams-test-utils:$kafkaVersion"
    )

    val testingMockk = listOf(
        "io.mockk:mockk:$mockkVersion"
    )

    set("kotlinVersion", kotlinVersion)
    set("http4kVersion", http4kVersion)
    set("jwtVersion", jwtVersion)
    set("junitVersion", junitVersion)
    set("jsonUnitVersion", jsonUnitVersion)
    set("mockkVersion", mockkVersion)
    set("jacksonVersion", jacksonVersion)
    set("jetbrainsExposedVersion", jetbrainsExposedVersion)
    set("postgresVersion", postgresVersion)
    set("kafkaVersion", kafkaVersion)

    set("jackson", jackson)
    set("kotlin", kotlin)
    set("http4kJson", http4kJson)
    set("http4k", http4k)
    set("http4k_client", http4k_client)
    set("jwt", jwt)
    set("database", database)
    set("kafka", kafka)
    set("testingLibs", testingLibs)
    set("testingJsonUnit", testingJsonUnit)
    set("testingKafka", testingKafka)
    set("testingMockk", testingMockk)
}