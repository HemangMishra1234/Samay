package com.project.samay.presentation

import kotlinx.serialization.Serializable

sealed interface Destinations {
    @Serializable
    data object MeditationScreen: Destinations

    @Serializable
    data object BackupScreen: Destinations

}