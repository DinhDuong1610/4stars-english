package com.fourstars.fourstars_english.model

data class Vocabulary(
    val word: String,
    val ipa: String,
    val part_of_speech: String,
    val definition: String,
    val translation: String,
    val examples: String?,
    val category: String?,
    val imgUrl: String?,
    val audio: String?,
)
