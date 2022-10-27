package com.plcourse.mkirilkin.util

import java.io.File

val words = readWordList("resources/programmers_wordlist.txt")

fun readWordList(fileName: String): List<String> {
    val inputStream = File(fileName).inputStream()
    val words = mutableListOf<String>()
    inputStream.bufferedReader().forEachLine { words.add(it.trim()) }
    return words
}


fun getRandomWords(amount: Int): List<String> {
    var curAmount = 0
    val result = mutableListOf<String>()
    while (curAmount < amount && curAmount != words.size) {
        val word = words.random()
        if (!result.contains(word)) {
            result.add(word)
            curAmount++
        }
    }
    return result
}

// apple juice
// _ _ _ _ _   _ _ _ _ _
fun String.transformToUnderscores() =
    toCharArray().map {
        if (it != ' ') '_' else ' '
    }.joinToString(" ")
