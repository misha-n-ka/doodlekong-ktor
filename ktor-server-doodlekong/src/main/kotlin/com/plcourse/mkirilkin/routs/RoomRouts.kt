package com.plcourse.mkirilkin.routs

import com.plcourse.mkirilkin.data.Room
import com.plcourse.mkirilkin.data.models.BasicApiResponse
import com.plcourse.mkirilkin.data.models.CreateRoomRequest
import com.plcourse.mkirilkin.data.models.RoomResponse
import com.plcourse.mkirilkin.server
import com.plcourse.mkirilkin.util.Constants
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.createRoomRoute() {
    route("/api/createRoom") {
        post {
            val roomRequest = call.receiveOrNull<CreateRoomRequest>() // получение запроса в качестве модели
            if (roomRequest == null) {
                call.respond(HttpStatusCode.BadRequest)
                return@post
            }
            if (server.rooms[roomRequest.name] != null) { // комнаты с переданным имененм не существует
                call.respond(
                    HttpStatusCode.OK,
                    BasicApiResponse(false, "Room already exists")
                )
                return@post
            }
            if (roomRequest.maxPlayers < 2) {
                call.respond(
                    HttpStatusCode.OK,
                    BasicApiResponse(false, "The minimum size is 2")
                )
                return@post
            }
            if (roomRequest.maxPlayers > Constants.MAX_ROOM_SIZE) {
                call.respond(
                    HttpStatusCode.OK,
                    BasicApiResponse(false, "The maximum room size is ${Constants.MAX_ROOM_SIZE}")
                )
                return@post
            }

            val room = Room(
                roomRequest.name,
                roomRequest.maxPlayers
            )
            server.rooms[roomRequest.name] = room
            println("Room created: ${roomRequest.name}")

            call.respond(
                HttpStatusCode.OK,
                BasicApiResponse(true, "Room was created successfully")
            )
        }
    }
}

fun Route.getRoomsRoute() {
    route("api/getRooms") {
        get {
            val searchQuery = call.parameters["searchQuery"]
            if (searchQuery == null) {
                call.respond(HttpStatusCode.BadRequest)
                return@get
            }

            val roomResult = server.rooms.filterKeys {
                it.contains(searchQuery, ignoreCase = true)
            }
            val roomResponse = roomResult.values.map {
                RoomResponse(it.name, it.maxPlayers, it.players.size)
            }.sortedBy { it.name }

            call.respond(HttpStatusCode.OK, roomResponse)
        }
    }
}
