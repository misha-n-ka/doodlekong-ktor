package com.plcourse.mkirilkin.data.models.messages

import com.plcourse.mkirilkin.data.Room
import com.plcourse.mkirilkin.util.Constants.TYPE_PHASE_CHANGE

data class PhaseChange(
    var phase: Room.Phase?,
    var time: Long,
    val drawingPlayer: String? = null
) : BaseModel(TYPE_PHASE_CHANGE)
