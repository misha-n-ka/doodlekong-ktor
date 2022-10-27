package com.plcourse.mkirilkin.data.models.messages

import com.plcourse.mkirilkin.util.Constants.TYPE_GAME_STATE

data class GameState(
    val drawingPlayer: String,
    val word: String,
) : BaseModel(TYPE_GAME_STATE)
