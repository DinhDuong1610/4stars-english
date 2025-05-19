package com.fourstars.fourstars_english.model

data class Article(
    val id: String = "",
    val title: String = "",
    val publishDate: String = "",
    val content: String = "",
    val imageUrl: String? = null,
    val audioUrl: String? = null
)
