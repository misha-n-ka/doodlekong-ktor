package com.plcourse.mkirilkin.data

import com.plcourse.mkirilkin.data.models.messages.Ping
import com.plcourse.mkirilkin.gson
import com.plcourse.mkirilkin.server
import com.plcourse.mkirilkin.util.Constants.PING_PERIOD
import io.ktor.http.cio.websocket.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

data class Player(
    val userName: String,
    var socket: WebSocketSession,
    val clientId: String,
    var isDrawing: Boolean = false,
    var score: Int = 0,
    var rank: Int = 0
) {
    private var pingJob: Job? = null

    private var pingTime = 0L
    private var pongTime = 0L

    var isOnline = true

    fun startPinging() {
        pingJob?.cancel()
        pingJob = GlobalScope.launch {
            while (true) {
                sendPing()
                delay(PING_PERIOD)
            }
        }
    }

    fun receivedPong() {
        pongTime = System.currentTimeMillis()
        isOnline = true
    }

    fun disconnect() {
        pingJob?.cancel()
    }

    private suspend fun sendPing() {
        pingTime = System.currentTimeMillis()
        socket.send(Frame.Text(gson.toJson(Ping())))
        delay(PING_PERIOD)
        if (pingTime - pongTime > PING_PERIOD) {
            isOnline = false
            server.playerLeft(clientId)
            pingJob?.cancel()
        }
    }
}
