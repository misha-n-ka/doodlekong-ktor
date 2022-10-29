package com.plcourse.mkirilkin.data.models.messages

fun ChatMessage.matchesWord(word: String): Boolean {
    return message.lowercase().trim() == word.lowercase().trim()
}
