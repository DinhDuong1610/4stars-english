package com.fourstars.fourstars_english.model

data class Vocabulary(
    val word: String,
    val ipa: String,
    val part_of_speech: String,
    val definition: String,
    val translation: String,
    val examples: List<Example>,
    val audio: String,
    val category: String,
    val image: String
)

data class Example(
    val en: String,
    val vi: String
)