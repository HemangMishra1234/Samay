package com.project.samay.domain.mediaplayer

import android.content.Context
import android.media.MediaPlayer

class MusicPlayer(private val context: Context){
    private var mediaPlayer: MediaPlayer? = null

    fun play(resourceId: Int){
        if(mediaPlayer == null){
            mediaPlayer = MediaPlayer.create(context, resourceId)
        }
        mediaPlayer?.start()
    }

    fun pause(){
        mediaPlayer?.pause()
    }

    fun stop(){
        mediaPlayer = null
    }
}