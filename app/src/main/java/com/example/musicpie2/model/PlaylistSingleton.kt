package com.example.musicpie2.model

import android.util.Log

object PlaylistSingleton {
    var playlist = arrayListOf<Song>()

    fun addSong(song: Song) {
        playlist.add(song)
    }

    fun removeSong(position: Int) {
        playlist.removeAt(position)
    }

    fun updatePlaylist(updatedPlaylist: ArrayList<Song>) {
        playlist = updatedPlaylist
    }

    fun getSize(): Int {
        return playlist.size
    }

    fun clear() {
        playlist = arrayListOf()
    }
}