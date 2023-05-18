package com.example.musicpie2.viewmodel

import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.navigation.NavController
import com.example.musicpie2.R
import com.example.musicpie2.model.MediaPlayerSingleton
import com.example.musicpie2.model.Song
import com.example.musicpie2.view.ui.player.PlayerUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class PlayerViewModel : ViewModel() {
    private var isPlaying: Boolean = true
    private var isRandom: Boolean = false
    private var destroyMediaPlayer: Boolean = false
    private var playlist: ArrayList<Song> = arrayListOf()
    private val baseSongIndex = 0
    private var currentPosition = baseSongIndex
    private var mediaPlayerSingleton = MediaPlayerSingleton

    private val _uiState = MutableStateFlow(PlayerUiState())
    val uiState = _uiState.asStateFlow()
    private var navController: NavController? = null
    private var arguments: Bundle? = null

    private lateinit var homeViewModel: HomeViewModel
    private var broadcastAction = "com.example.musicpie2.view.ui.PLAYER_BROADCAST"

    fun setNavController(navController: NavController) {
        this.navController = navController
    }

    fun setArguments(arguments: Bundle) {
        this.arguments = arguments
    }

    fun initializeVariables(owner: ViewModelStoreOwner) {
        homeViewModel = ViewModelProvider(owner)[HomeViewModel::class.java]
        playlist = homeViewModel.getSongsList()
        isPlaying = arguments?.getBoolean("isPlaying") ?: true
        isRandom = arguments?.getBoolean("isRandom") ?: false
        destroyMediaPlayer = arguments?.getBoolean("destroyMediaPlayer") ?: false
        currentPosition = arguments?.getInt("currentPosition") ?: baseSongIndex

        _uiState.update {
            it.copy(isPlaying = isPlaying, currentPosition = currentPosition, destroyMediaPlayer = destroyMediaPlayer)
        }
    }

    fun checkDestroyMediaPlayer() {
        if (destroyMediaPlayer) {
            //onDestroy()
            destroy()
            destroyMediaPlayer = false
        }
        _uiState.update {
            it.copy(destroyMediaPlayer = destroyMediaPlayer)
        }
    }

    fun destroy() {
        mediaPlayerSingleton.mediaPlayer?.stop()
        mediaPlayerSingleton.release()
    }


    fun checkMediaPlayer(context: Context) {
        mediaPlayerSingleton.mediaPlayer?.let {
            //initSeekbar()
        }
        if (isPlaying) {
            if (mediaPlayerSingleton.mediaPlayer == null) {
                mediaPlayerSingleton.init(context, playlist[currentPosition].audio)
            }
            sendToastBroadcast("Playing \"${playlist[currentPosition].songTitle}\"",context)
            mediaPlayerSingleton.start()
            //initSeekbar()
        } else {
            mediaPlayerSingleton.pause()
        }
    }



    fun playPauseOnClick(context: Context) {
        isPlaying = _uiState.value.isPlaying

        if (mediaPlayerSingleton.isPlaying()) {
            mediaPlayerSingleton.pause()
            isPlaying = false
        } else {
            if (mediaPlayerSingleton.mediaPlayer == null) {
                mediaPlayerSingleton.init(context, playlist[currentPosition].audio)
            }
            mediaPlayerSingleton.start()
            isPlaying = true
            // initSeekbar()
            sendToastBroadcast("Playing \"${playlist[currentPosition].songTitle}\"",context)
        }

        _uiState.update {
            it.copy(isPlaying = isPlaying)
        }
    }

    fun onNextClick(context: Context) {
        mediaPlayerSingleton.mediaPlayer?.pause()
        if (currentPosition < playlist.size - 1) {
            currentPosition++
        } else {
            currentPosition = 0
        }

        mediaPlayerSingleton.mediaPlayer =
            MediaPlayer.create(context, playlist[currentPosition].audio)
        mediaPlayerSingleton.mediaPlayer?.start()
        sendToastBroadcast("Playing \"${playlist[currentPosition].songTitle}\"",context)
        isPlaying = true

        _uiState.update {
            it.copy(isPlaying = isPlaying, currentPosition = currentPosition)
        }
    }


    fun onPreviousClick(context: Context) {
        mediaPlayerSingleton.mediaPlayer?.pause()

        if (currentPosition > 0) {
            currentPosition--
        } else {
            currentPosition = playlist.size - 1
        }
        mediaPlayerSingleton.mediaPlayer =
            MediaPlayer.create(context, playlist[currentPosition].audio)
        mediaPlayerSingleton.mediaPlayer?.start()
        isPlaying = true

        _uiState.update {
            it.copy(isPlaying = isPlaying, currentPosition = currentPosition)
        }
    }

    fun navigationToSettings() {
        mediaPlayerSingleton.pause()
        isPlaying = false

        navController?.navigate(R.id.action_playerFragment_to_settingsFragment)

        _uiState.update {
            it.copy(isPlaying = isPlaying)
        }
    }

    fun navigationToBack() {
        val bundle = Bundle()
        bundle.putBoolean("isPlaying", isPlaying)
        bundle.putBoolean("isRandom", isRandom)
        bundle.putBoolean("destroyMediaPlayer", destroyMediaPlayer)
        bundle.putInt("currentPosition", currentPosition)
        navController?.navigate(R.id.action_playerFragment_to_homeFragment, bundle)
    }

    private fun sendToastBroadcast(message: String, context: Context) {
        val intent = Intent()
        intent.action = broadcastAction
        intent.putExtra("message", message)
        context.sendBroadcast(intent)
    }
}