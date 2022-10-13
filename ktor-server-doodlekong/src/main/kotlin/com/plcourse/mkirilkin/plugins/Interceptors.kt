package com.plcourse.mkirilkin.plugins

import com.plcourse.mkirilkin.session.DrawingSession
import io.ktor.application.*
import io.ktor.sessions.*
import io.ktor.util.*

fun Application.configureInterceptors() {
    intercept(ApplicationCallPipeline.Features) {
        if (call.sessions.get<DrawingSession>() == null) {
            val clientId = call.parameters["client_id"].orEmpty()
            call.sessions.set(
                DrawingSession(
                    clientId = clientId,
                    sessionId = generateNonce()
                )
            )
        }
    }
}
