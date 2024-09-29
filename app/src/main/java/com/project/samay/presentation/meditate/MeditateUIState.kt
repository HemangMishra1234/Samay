package com.project.samay.presentation.meditate

data class MeditateUIState(
    val currentProgress: Float = 0.0f,
    val totalLength: Long = 3600,
    val isPlaying: Boolean = false,
    val currentAudioId: Int = 2
)