package com.plcourse.mkirilkin.data.models.messages

import com.plcourse.mkirilkin.data.PlayerData
import com.plcourse.mkirilkin.util.Constants.TYPE_PLAYERS_LIST

data class PlayersList(
    val players: List<PlayerData>
) : BaseModel(TYPE_PLAYERS_LIST)
