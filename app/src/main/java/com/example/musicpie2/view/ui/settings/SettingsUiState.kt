package com.example.musicpie2.view.ui.settings

import com.example.musicpie2.model.Song

data class SettingsUiState (
    val songsList: List<Song> = emptyList()
)