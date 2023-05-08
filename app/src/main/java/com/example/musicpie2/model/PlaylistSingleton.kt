package com.example.musicpie2.model

object PlaylistSingleton {
    val playlist = arrayListOf<Song>()

    fun addSong(song: Song) {
        playlist.add(song)
    }

    fun removeSong(song: Song) {
        playlist.remove(song)
    }

    fun getSize(): Int {
        return playlist.size
    }
}