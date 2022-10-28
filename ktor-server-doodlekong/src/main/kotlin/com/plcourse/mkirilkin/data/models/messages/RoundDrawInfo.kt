package com.plcourse.mkirilkin.data.models.messages

import com.plcourse.mkirilkin.util.Constants.TYPE_CURRENT_ROUND_DRAW_INFO

data class RoundDrawInfo(
    val data: List<String>
) : BaseModel(TYPE_CURRENT_ROUND_DRAW_INFO)
