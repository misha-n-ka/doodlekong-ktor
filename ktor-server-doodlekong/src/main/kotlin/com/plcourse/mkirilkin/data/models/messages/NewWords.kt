package com.plcourse.mkirilkin.data.models.messages

import com.plcourse.mkirilkin.util.Constants.TYPE_NEW_WORDS

data class NewWords(
    val newWords: List<String>
) : BaseModel(TYPE_NEW_WORDS)
