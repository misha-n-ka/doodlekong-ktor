package com.plcourse.mkirilkin

import com.plcourse.mkirilkin.plugins.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*

val server = DrawingServer()

fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0") {
        configureSessions()
        configureInterceptors()
        configureSerialization()
        configureSockets()
        configureMonitoring()
        configureRouting()
    }.start(wait = true)
}
