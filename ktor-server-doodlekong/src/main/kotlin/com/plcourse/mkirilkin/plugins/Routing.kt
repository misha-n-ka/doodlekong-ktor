package com.plcourse.mkirilkin.plugins

import com.plcourse.mkirilkin.routs.createRoomRoute
import io.ktor.application.*
import io.ktor.routing.*

fun Application.configureRouting() {
    install(Routing) {
        createRoomRoute()
    }
}
