package com.dentmanage.service

import org.http4k.core.HttpHandler
import org.http4k.server.Http4kServer
import java.util.concurrent.atomic.AtomicBoolean

interface SimpleLifecycle {
    fun start() {}
    fun stop() {}
}

class RestServer(
    api: HttpHandler,
    private val server: Http4kServer,
    private val service: SimpleLifecycle,
    private val onClose: () -> Unit
) : HttpHandler by api, Http4kServer by server {
    private val stopped = AtomicBoolean(false)

    override fun start(): RestServer = apply {
        withServiceStartedLogging {
            service.start()
            server.start()
//            events
        }
        stopped.set(false)
    }

    override fun stop(): RestServer = apply {
        stopServiceIfNotAlreadyStopped {
            withServiceStoppedLogging {
                server.stop()
                service.stop()
            }
        }
    }

    override fun close() {
        this.stop()
        onClose()
    }

    private fun stopServiceIfNotAlreadyStopped(block: () -> Unit) {
        if (stopped.compareAndSet(false, true)) {
            block()
        }
    }

    private fun withServiceStartedLogging(block: () -> Unit) {
        return try {
            block()
//            events
        } catch (e: Exception) {
//            events
            throw e
        }
    }

    private fun withServiceStoppedLogging(block: () -> Unit) {
        return try {
            block()
        } finally {
//            events
        }
    }
}

fun RestServer.startServerAndStopOnShutdown(shutdownHook: () -> Unit) {
    start()
    Runtime.getRuntime().addShutdownHook(Thread {
        stop()
        shutdownHook()
    })
}

// Todo
//private fun createApi(
//    infrastructure: Serv
//)