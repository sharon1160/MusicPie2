package com.example.musicpie2.view.ui.player

data class PlayerUiState(
    val isPlaying: Boolean = false,
    val currentPosition: Int = 0,
    val destroyMediaPlayer: Boolean = false
)