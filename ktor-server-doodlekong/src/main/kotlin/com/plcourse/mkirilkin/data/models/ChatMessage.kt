package com.plcourse.mkirilkin.data.models

import com.plcourse.mkirilkin.util.Constants

data class ChatMessage(
    val from: String,
    val room: String,
    val message: String,
    val timeStamp: Long
) : BaseModel(Constants.TYPE_CHAT_MESSAGE)
