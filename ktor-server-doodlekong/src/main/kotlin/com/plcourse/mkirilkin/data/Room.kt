package com.plcourse.mkirilkin.data

import com.plcourse.mkirilkin.data.models.messages.Announcement
import com.plcourse.mkirilkin.data.models.messages.PhaseChange
import com.plcourse.mkirilkin.gson
import io.ktor.http.cio.websocket.*
import kotlinx.coroutines.*

class Room(
    val name: String,
    val maxPlayers: Int,
    var players: List<Player> = emptyList()
) {

    private var timerJob: Job? = null
    private var drawingPlayer: Player? = null

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

    private fun timeAndNotify(ms: Long) {
        timerJob?.cancel()
        timerJob = GlobalScope.launch {
            val phaseChange = PhaseChange(
                phase,
                ms,
                drawingPlayer?.userName
            )
            repeat((ms / UPDATE_TIME_FREQUENCY).toInt()) {
                if (it > 0) {
                    phaseChange.phase = null
                }
                broadcast(gson.toJson(phaseChange))
                phaseChange.time -= UPDATE_TIME_FREQUENCY
                delay(UPDATE_TIME_FREQUENCY)
            }
            phase = when(phase) {
                Phase.WAITING_FOR_PLAYERS -> Phase.NEW_ROUND
                Phase.GAME_RUNNING -> Phase.SHOW_WORD
                Phase.SHOW_WORD -> Phase.NEW_ROUND
                Phase.NEW_ROUND -> Phase.GAME_RUNNING
                else -> Phase.WAITING_FOR_PLAYERS
            }
        }
    }

    private fun waitingForPlayers() {
        GlobalScope.launch {
            val phaseChange = PhaseChange(
                Phase.WAITING_FOR_PLAYERS,
                DElAY_WAITING_FOR_START_TO_NEW_ROUND,
            )
            broadcast(gson.toJson(phaseChange))
        }
    }

    private fun waitingForStart() {
        GlobalScope.launch {
            timeAndNotify(DElAY_WAITING_FOR_START_TO_NEW_ROUND)
            val phaseChange = PhaseChange(
                Phase.WAITING_FOR_START,
                DElAY_WAITING_FOR_START_TO_NEW_ROUND,
            )
            broadcast(gson.toJson(phaseChange))
        }
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

    companion object {

        const val UPDATE_TIME_FREQUENCY = 1_000L // millis in one second

        const val DElAY_WAITING_FOR_START_TO_NEW_ROUND = 10_000L
        const val DELAY_NEW_ROUND_TO_GAME_RUNNING = 20_000L
        const val DELAY_GAME_RUNNING_TO_SHOW_WORD = 60_000L
        const val SHOW_WORD_TO_NEW_ROUND = 10_000L
    }
}
