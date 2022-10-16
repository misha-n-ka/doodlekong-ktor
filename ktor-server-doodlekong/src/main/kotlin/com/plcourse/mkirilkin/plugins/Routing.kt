package com.plcourse.mkirilkin.plugins

import com.plcourse.mkirilkin.routs.createRoomRoute
import com.plcourse.mkirilkin.routs.getRoomsRoute
import com.plcourse.mkirilkin.routs.joinRoomRoute
import io.ktor.application.*
import io.ktor.routing.*

fun Application.configureRouting() {
    install(Routing) {
        createRoomRoute()
        getRoomsRoute()
        joinRoomRoute()
    }
}
