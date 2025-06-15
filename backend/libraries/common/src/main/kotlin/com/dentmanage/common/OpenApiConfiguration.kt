package com.dentmanage.common

import org.http4k.format.ConfigurableJackson

data class OpenApiConfiguration(
    val version: ContractVersion,
    val json: ConfigurableJackson = DentJson,
    val nameProvider: (ServiceName) -> String = { "${it.value} API" },
    val descriptionProvider: (ServiceName) -> String = { "OpenApi spec for the ${it.value}" }
)