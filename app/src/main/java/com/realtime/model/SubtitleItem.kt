package com.realtime.model

data class SubtitleItem(
    val text: String,
    val translatedText: String? = null,
    val startMs: Long,
    val endMs: Long
)
