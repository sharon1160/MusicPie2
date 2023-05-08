package com.example.musicpie2.viewmodel

import androidx.lifecycle.ViewModel
import com.example.musicpie2.model.PlaylistSingleton
import com.example.musicpie2.model.Song

class SongViewModel: ViewModel() {
    private val playlistSingleton = PlaylistSingleton

    fun getSongsList(): ArrayList<Song> {
        return playlistSingleton.playlist
    }

    fun addSong(song: Song) {
        playlistSingleton.addSong(song)
    }

    fun removeSong(song: Song) {
        playlistSingleton.removeSong(song)
    }

    fun getSize(): Int {
        return playlistSingleton.getSize()
    }
}