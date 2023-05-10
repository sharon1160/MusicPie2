package com.example.musicpie2.model

import android.content.Context
import android.media.MediaPlayer
import android.net.Uri

object MediaPlayerSingleton {
    var mediaPlayer: MediaPlayer? = null

    fun init(context: Context, uri: Uri) {
        mediaPlayer = MediaPlayer.create(context, uri)
    }

    fun release() {
        mediaPlayer?.release()
        mediaPlayer = null
    }

    fun start() {
        mediaPlayer?.start()
    }

    fun pause() {
        mediaPlayer?.pause()
    }

    fun isPlaying(): Boolean {
        return mediaPlayer?.isPlaying ?: false
    }
}