package com.plcourse.mkirilkin.plugins

import com.plcourse.mkirilkin.session.DrawingSession
import io.ktor.application.*
import io.ktor.sessions.*

fun Application.configureSessions() {
    install(Sessions) {
        cookie<DrawingSession>("SESSION")
    }

}
