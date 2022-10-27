package com.plcourse.mkirilkin

import com.plcourse.mkirilkin.data.Player
import com.plcourse.mkirilkin.data.Room
import java.util.concurrent.ConcurrentHashMap

class DrawingServer {

    val rooms = ConcurrentHashMap<String, Room>()
    val players = ConcurrentHashMap<String, Player>()

    fun playerJoined(player: Player) {
        players[player.clientId] = player
    }

    fun getRoomWithClientId(clientId: String): Room? {
        val filteredRooms = rooms.filterValues { room ->
            room.players.find { player ->
                player.clientId == clientId
            } != null
        }
        return if (filteredRooms.values.isEmpty()) {
            null
        } else {
            filteredRooms.values.toList().first()
        }
    }

    fun playerLeft(clientId: String, immediatelyDisconnect: Boolean = false) {
        val playersRoom = getRoomWithClientId(clientId)
        if (immediatelyDisconnect) {
            println("Closing connection to ${players[clientId]?.userName}")
            playersRoom?.removePlayer(clientId)
            players.remove(clientId)
        }
    }
}
