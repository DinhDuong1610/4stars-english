package com.fourstars.fourstars_english.model

data class Comment(
    val userId: String = "",
    val userName: String = "",
    val content: String = "",
    val timestamp: Long = System.currentTimeMillis(),
    val userAvatar: String = ""
)
