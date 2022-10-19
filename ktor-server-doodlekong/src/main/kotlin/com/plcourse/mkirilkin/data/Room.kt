package com.plcourse.mkirilkin.data

import com.plcourse.mkirilkin.data.models.messages.Announcement
import com.plcourse.mkirilkin.gson
import io.ktor.http.cio.websocket.*
import kotlinx.coroutines.isActive

class Room(
    val name: String,
    val maxPlayers: Int,
    var players: List<Player> = emptyList()
) {

    private var phaseChangedListener: ((Phase) -> Unit)? = null
    var phase = Phase.WAITING_FOR_PLAYERS
        set(value) {
            synchronized(field) {
                field = value
                phaseChangedListener?.let { change ->
                    change(value)
                }
            }
        }

    init {
        setPhaseChangedListener { newPhase ->
            when (newPhase) {
                Phase.WAITING_FOR_PLAYERS -> waitingForPlayers()
                Phase.WAITING_FOR_START -> waitingForStart()
                Phase.NEW_ROUND -> newRound()
                Phase.GAME_RUNNING -> gameRunning()
                Phase.SHOW_WORD -> showWord()
            }
        }
    }

    private fun setPhaseChangedListener(listener: (Phase) -> Unit) {
        phaseChangedListener = listener
    }

    suspend fun broadcast(message: String) {
        players.forEach { player ->
            if (player.socket.isActive) {
                player.socket.send(Frame.Text(message))
            }
        }
    }

    suspend fun broadcastToAllExcept(message: String, clientId: String) {
        players.forEach { player ->
            if (player.clientId != clientId && player.socket.isActive) {
                player.socket.send(Frame.Text(message))
            }
        }
    }

    suspend fun addPlayer(clientId: String, userName: String, socket: WebSocketSession): Player {
        val player = Player(userName, socket, clientId)
        players = players + player

        if (players.size == 1) { // Единственный кто зашел в команту
            phase = Phase.WAITING_FOR_PLAYERS
        } else if (players.size == 2 && phase == Phase.WAITING_FOR_PLAYERS) {
            // При двух игроках в комнате ждем начала игры
            phase = Phase.WAITING_FOR_START
            players = players.shuffled()
        } else if (phase == Phase.WAITING_FOR_START && players.size == maxPlayers) {
            phase = Phase.NEW_ROUND
            players = players.shuffled()
        }

        val announcement = Announcement(
            "$userName joined the party!",
            System.currentTimeMillis(),
            Announcement.TYPE_PLAYER_JOINED
        )

        broadcast(gson.toJson(announcement))

        return player
    }

    fun containsPlayer(username: String): Boolean {
        return players.find { it.userName == username } != null
    }

    private fun waitingForPlayers() {

    }

    private fun waitingForStart() {

    }

    private fun newRound() {

    }

    private fun gameRunning() {

    }

    private fun showWord() {

    }


    enum class Phase {
        WAITING_FOR_PLAYERS,
        WAITING_FOR_START,
        NEW_ROUND,
        GAME_RUNNING,
        SHOW_WORD
    }
}
