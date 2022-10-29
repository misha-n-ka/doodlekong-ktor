package com.plcourse.mkirilkin.data.models.messages

import com.plcourse.mkirilkin.util.Constants.TYPE_CHOSEN_WORD

data class ChosenWord(
    val chosenWord: String,
    val roomName: String
) : BaseModel(TYPE_CHOSEN_WORD)
