package com.fourstars.fourstars_english.model

data class Video(
    val title: String = "",
    val duration: String = "",
    val datePosted: String = "",
    val thumbnailUrl: String = "",
    val videoId: String = "",
    val subtitles: List<SubtitleLine> = emptyList()
)

data class SubtitleLine(
    val timeStart: Float = 0f,
    val timeEnd: Float = 0f,
    val viText: String = "",
    val enText: String = "",
    val isHighlighted: Boolean = false
)
