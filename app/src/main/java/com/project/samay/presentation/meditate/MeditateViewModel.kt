package com.project.samay.presentation.meditate

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.project.samay.domain.mediaplayer.MusicPlayer

class MeditateViewModel(private val musicPlayer: MusicPlayer): ViewModel() {

    private var _meditateUIState = mutableStateOf(MeditateUIState())
    val meditateUIState: State<MeditateUIState> = _meditateUIState

    fun changeProgress(progress: Float){
        _meditateUIState.value = meditateUIState.value.copy(currentProgress = progress)
    }

    fun onClickPlayPauseButton(){
        if(_meditateUIState.value.isPlaying)
            musicPlayer.pause()
        else
            musicPlayer.play(_meditateUIState.value.currentAudioId)
    }


}