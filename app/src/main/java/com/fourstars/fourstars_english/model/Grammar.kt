package com.fourstars.fourstars_english.model

data class GrammarExample(
    val sentence: String,
    val translation: String
)

data class Grammar(
    val phrase: String,
    val structure: String,
    val meaning: String,
    val explanation: String,
    val examples: List<GrammarExample>,
    val category: String
)
