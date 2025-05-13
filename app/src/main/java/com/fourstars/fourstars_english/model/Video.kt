package com.fourstars.fourstars_english.model

data class Video(
    val title: String,
    val duration: String,
    val datePosted: String,
    val thumbnailUrl: String,
    val videoId: String,
    val subtitles: List<SubtitleLine>
)

data class SubtitleLine(
    val timeStart: Float,
    val timeEnd: Float,
    val viText: String,
    val enText: String,
    val isHighlighted: Boolean = false
)
