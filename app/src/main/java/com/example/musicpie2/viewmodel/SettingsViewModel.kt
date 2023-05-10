package com.example.musicpie2.viewmodel

import android.content.ContentResolver
import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.musicpie2.model.PlaylistSingleton
import com.example.musicpie2.model.PlaylistSingleton.addSong
import com.example.musicpie2.model.Song
import com.example.musicpie2.model.SongFileContentProvider

class SettingsViewModel : ViewModel() {
    private var playlistSingleton = PlaylistSingleton
    private var allPlaylist = arrayListOf<Song>()

    fun loadAllSongsList(contentResolver: ContentResolver) {
        val songProvider = SongFileContentProvider()
        val cursor = contentResolver.query(
            SongFileContentProvider.CONTENT_URI,
            null,
            null,
            null,
            null
        )
        cursor?.let {
            allPlaylist = songProvider.getPlaylist(cursor)
            updateAllPlaylist()
        }
    }

    private fun updateAllPlaylist() {
        playlistSingleton.playlist.forEach {
            val position =
                allPlaylist.indexOfFirst { song ->
                    song.songTitle == it.songTitle && song.songArtist == it.songArtist
                }
            allPlaylist.removeAt(position)
        }
        allPlaylist.forEach {
            val position =
                allPlaylist.indexOfFirst { song ->
                    song.songTitle == it.songTitle && song.songArtist == it.songArtist
                }
            allPlaylist[position].inPlaylist = false
        }
        val result = ArrayList<Song>()
        result.addAll(playlistSingleton.playlist)
        result.addAll(allPlaylist)
        allPlaylist = result
    }

    fun getAllSongsList(): ArrayList<Song> {
        return allPlaylist
    }

    fun removeSong(song: Song, position: Int) {
        playlistSingleton.removeSong(position)
        updateAllSongsListAfterDelete(song, position)
    }

    fun addSong(song: Song, position: Int, lastPositionTrue: Int) {
        playlistSingleton.addSong(song)
        updateAllSongsListAfterAdd(song, position, lastPositionTrue)
    }

    private fun updateAllSongsListAfterDelete(song: Song, position: Int) {
        song.inPlaylist = false
        allPlaylist.removeAt(position)
        allPlaylist.add(song)
    }

    private fun updateAllSongsListAfterAdd(song: Song, position: Int, lastPositionTrue: Int) {
        song.inPlaylist = true
        allPlaylist.removeAt(position)
        allPlaylist.add(lastPositionTrue + 1, song)
    }

    fun getLastTruePosition(): Int {
        var lastSongIndex = allPlaylist.asReversed().indexOfFirst { it.inPlaylist == true }
        lastSongIndex = allPlaylist.size - lastSongIndex - 1
        return lastSongIndex
    }

    fun getAllSongsListSize(): Int {
        return allPlaylist.size
    }

}