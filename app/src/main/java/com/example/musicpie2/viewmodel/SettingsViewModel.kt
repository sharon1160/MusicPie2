package com.example.musicpie2.viewmodel

import android.content.ContentResolver
import android.os.Bundle
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.musicpie2.R
import com.example.musicpie2.model.MediaPlayerSingleton
import com.example.musicpie2.model.PlaylistSingleton
import com.example.musicpie2.model.Song
import com.example.musicpie2.model.SongFileContentProvider
import com.example.musicpie2.view.ui.settings.SettingsUiState
import kotlinx.coroutines.flow.*

class SettingsViewModel : ViewModel() {
    private var playlistSingleton = PlaylistSingleton
    private var allPlaylist = arrayListOf<Song>()

    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState = _uiState.asStateFlow()
    private var navController: NavController? = null
    private var mediaPlayerSingleton = MediaPlayerSingleton

    fun setNavController(navController: NavController) {
        this.navController = navController
    }

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
        _uiState.update {
            it.copy(songsList = allPlaylist)
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

    fun onActionClick(songTitle: String) {
        destroy()
        val songList = _uiState.value.songsList
        val selectedSong: Song? = songList.find { it.songTitle == songTitle }
        val index: Int = songList.indexOf(selectedSong)
        selectedSong?.let { song ->

            val newList = songList.toMutableList().apply {
                this[index] = song.copy(inPlaylist = !song.inPlaylist)
            }
            _uiState.update {
                it.copy(songsList = newList)
            }
        }
    }

    fun onBackClick() {
        val updatedPlaylist: List<Song> = _uiState.value.songsList.filter { it.inPlaylist }
        playlistSingleton.updatePlaylist(ArrayList(updatedPlaylist))

        val bundle = Bundle()
        allPlaylist = allPlaylist.filter { it.inPlaylist } as ArrayList<Song>
        bundle.putParcelableArrayList("initialPlaylist",allPlaylist)
        navController?.navigate(R.id.action_settingsFragment_to_homeFragment, bundle)
    }

    private fun destroy() {
        mediaPlayerSingleton.mediaPlayer?.stop()
        mediaPlayerSingleton.release()
    }

}