package com.plcourse.mkirilkin.data.models.messages

import com.plcourse.mkirilkin.data.models.messages.BaseModel
import com.plcourse.mkirilkin.util.Constants
import com.plcourse.mkirilkin.util.Constants.TYPE_CHAT_MESSAGE

data class ChatMessage(
    val from: String,
    val room: String,
    val message: String,
    val timeStamp: Long
) : BaseModel(TYPE_CHAT_MESSAGE)
