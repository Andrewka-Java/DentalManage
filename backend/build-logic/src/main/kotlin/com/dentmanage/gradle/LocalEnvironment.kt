package com.dentmanage.gradle

import org.http4k.config.Environment
import org.http4k.config.MapEnvironment

class LocalEnvironment(
    private val environment: () -> Environment
) {
    private fun getVariable(name: String): String {
        return environment()[name] ?: error("Missing $name in local environment")
    }

    companion object {
        val localEnvironment get () = LocalEnvironment({ MapEnvironment.from(System.getenv().toProperties()) })

        fun onLocal(block: LocalEnvironment.() -> Unit) {
            localEnvironment.block()
        }
    }
}