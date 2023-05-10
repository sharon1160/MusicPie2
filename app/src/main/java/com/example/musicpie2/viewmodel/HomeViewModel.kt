package com.example.musicpie2.viewmodel

import android.content.ContentResolver
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.musicpie2.model.PlaylistSingleton
import com.example.musicpie2.model.Song
import com.example.musicpie2.model.SongFileContentProvider

class HomeViewModel : ViewModel() {
    private val playlistSingleton = PlaylistSingleton

    fun loadSongsList(contentResolver: ContentResolver) {
        val songProvider = SongFileContentProvider()
        val cursor = contentResolver.query(
            SongFileContentProvider.CONTENT_URI,
            null,
            null,
            null,
            null
        )
        cursor?.let {
            val playlist = songProvider.getPlaylist(cursor)
            playlist.forEach { if (it.inPlaylist) addSong(it) }
        }
    }

    fun getSongsList(): ArrayList<Song> {
        return playlistSingleton.playlist
    }

    fun addSong(song: Song) {
        playlistSingleton.addSong(song)
    }

    fun getSize(): Int {
        return playlistSingleton.getSize()
    }
}