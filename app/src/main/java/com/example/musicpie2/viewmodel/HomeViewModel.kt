package com.example.musicpie2.viewmodel

import android.content.ContentResolver
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import com.example.musicpie2.R
import com.example.musicpie2.model.MediaPlayerSingleton
import com.example.musicpie2.model.PlaylistSingleton
import com.example.musicpie2.model.Song
import com.example.musicpie2.model.SongFileContentProvider
import com.example.musicpie2.view.ui.home.HomeUiState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {
    private lateinit var playlist: ArrayList<Song>
    private lateinit var updatedPlaylist: ArrayList<Song>
    private var isRandom: Boolean = false
    private var isPlaying: Boolean = false
    private var destroyMediaPlayer: Boolean = false
    private val baseSongIndex = 0
    private var currentPosition = baseSongIndex
    private val playlistSingleton = PlaylistSingleton
    private var mediaPlayerSingleton = MediaPlayerSingleton
    private var arguments: Bundle? = null

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState = _uiState.asStateFlow()
    private var navController: NavController? = null

    fun setNavController(navController: NavController) {
        this.navController = navController
    }

    fun setArguments(arguments: Bundle) {
        this.arguments = arguments
    }

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

    private fun addSong(song: Song) {
        playlistSingleton.addSong(song)
    }

    fun initializeVariables() {
        playlist = getSongsList()
        isPlaying = arguments?.getBoolean("isPlaying") ?: false
        updatedPlaylist = arguments?.getParcelableArrayList("updatedPlaylist") ?: playlist
        currentPosition = arguments?.getInt("currentPosition") ?: baseSongIndex
        destroyMediaPlayer = arguments?.getBoolean("destroyMediaPlayer") ?: false
        _uiState.update {
            it.copy(isPlaying = isPlaying)
        }
    }

    fun playPauseOnClick() {
        isPlaying = _uiState.value.isPlaying
        Log.e("homeViewModel","isPlaying $isPlaying")
        isPlaying = !isPlaying
        isRandom = false

        _uiState.update {
            it.copy(isPlaying = isPlaying, isRandom = isRandom)
        }

        navigationToPlayer()
    }

    private fun navigationToPlayer() {
        val bundle = Bundle()
        bundle.putBoolean("isPlaying", isPlaying)
        bundle.putBoolean("isRandom", isRandom)
        bundle.putBoolean("destroyMediaPlayer", destroyMediaPlayer)
        bundle.putInt("currentPosition", currentPosition)
        navController?.navigate(R.id.action_homeFragment_to_playerFragment, bundle)
    }

    fun randomOnClick() {
        isRandom = _uiState.value.isRandom
        Log.e("homeViewModel","isRandom $isRandom")

        isRandom = if (isRandom) {
            currentPosition = 0
            false
        } else {
            currentPosition = (0 until playlist.size).random()
            true
        }
        isPlaying = false
        destroyMediaPlayer = true

        _uiState.update {
            it.copy(isRandom = isRandom, isPlaying = isPlaying)
        }
    }

    fun navigationToSettings() {
        Log.e("homeViewModel","Settings")

        mediaPlayerSingleton.pause()
        isPlaying = false

        _uiState.update {
            it.copy(isPlaying = isPlaying)
        }
        navController?.navigate(R.id.action_homeFragment_to_settingsFragment)
    }

    fun updateData() {
        viewModelScope.launch {
            _uiState.update {
                it.copy(isRefreshing = true)
            }
            delay(2000)
            playlistSingleton.updatePlaylist(updatedPlaylist)
            _uiState.update {
                it.copy(isRefreshing = false)
            }
        }
    }

}