package com.dentmanage.service

import com.dentmanage.common.ContractVersion
import com.dentmanage.common.DentJson
import com.dentmanage.common.OpenApiConfiguration
import com.dentmanage.common.ServiceConfiguration
import com.dentmanage.common.ServiceName
import org.http4k.core.HttpHandler
import org.http4k.core.Response
import org.http4k.core.Uri
import org.http4k.format.ConfigurableJackson

interface ServiceInfrastructure <T : ServiceConfiguration> {
    val config: T
    val responseFilter: (Response) -> Response
//    val runningInDevOrProd: Boolean
//    val applicationScope: CoroutineScope

    fun exitService(cause: Throwable): Nothing

    fun internalHttpClientFor(
        serviceName: ServiceName,
        baseUri: Uri
    ): HttpHandler =
        internalHttpClientFor(serviceName, clientOrNullForDefault = null, baseUri)

    fun internalHttpClientFor(
        serviceName: ServiceName,
        clientOrNullForDefault: HttpHandler? = null,
        baseUri: Uri? = null
    ): HttpHandler

    fun externalHttpClientFor(
        serviceName: ServiceName,
        clientOrNullForDefault: HttpHandler? = null,
        baseUri: Uri? = null
    ): HttpHandler

    fun close()
}

fun <T : ServiceConfiguration> ServiceInfrastructure<T>.openApiConfiguration(
    json: ConfigurableJackson = DentJson,
    nameProvider: (ServiceName) -> String = { "${it.value } API" },
    descriptionProvider: (ServiceName) -> String = { "OpenApi spec for the ${it.value}" },
    breakingVersion: Int = 0
) =
    OpenApiConfiguration(
        json = json,
        nameProvider = nameProvider,
        descriptionProvider = descriptionProvider,
        version = ContractVersion(breakingVersion, config.deployment.environment.id),
    )