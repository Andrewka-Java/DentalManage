package com.dentmanage.common

import org.http4k.config.Environment
import org.http4k.config.EnvironmentKey
import org.http4k.lens.int
import org.http4k.lens.string


interface ServiceConfiguration {
    val deployment: DeploymentInfo
    val serviceName: ServiceName
    val port: Int

    companion object {
        val HTTP_PORT = EnvironmentKey.int().defaulted("HTTP_PORT", 8092)
    }
}


data class DeploymentInfo(
    val environment: ServiceEnvironment,
    val podName: KubernetesPodName? = null,
    val podIp: KubernetesPodIp? = null
) {
    companion object {
        fun deploymentInfoFrom(environment: Environment): DeploymentInfo {
            return DeploymentInfo(
                environment = SERVICE_ENVIRONMENT(environment),
                podName = POD_NAME(environment),
                podIp = POD_IP(environment)
            )
        }

        val SERVICE_ENVIRONMENT = EnvironmentKey.string().map(ServiceEnvironment::fromId, ServiceEnvironment::id)
            .defaulted("SERVICE_ENVIRONMENT", ServiceEnvironment.LOCAL)
        val POD_NAME = EnvironmentKey.string().map(::KubernetesPodName, KubernetesPodName::value).optional("HOSTNAME")
        val POD_IP = EnvironmentKey.string().map(::KubernetesPodIp, KubernetesPodIp::value).optional("POD_IP")
    }
}


enum class ServiceEnvironment(val id: String) {
    LOCAL("local"),
    DEV("dev"),
    PRODUCTION("prod");

    companion object {
        fun fromId(id: String): ServiceEnvironment =
            ServiceEnvironment.entries.firstOrNull { it.id == id }
                ?: throw IllegalArgumentException("There is no ${ServiceEnvironment::class.simpleName} with id=$id")
    }
}

data class ContractVersion(val majorVersion: Int = 0, val tag: String = "") {
    override fun toString() = "$majorVersion.$tag"
}

class ServiceName(value: String) : StringValue(value)
class KubernetesPodName(value: String) : StringValue(value)
class KubernetesPodIp(value: String) : StringValue(value)