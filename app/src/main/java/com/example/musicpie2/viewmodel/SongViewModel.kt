package com.example.musicpie2.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.musicpie2.model.Song
import com.example.musicpie2.model.SongProvider

class SongViewModel: ViewModel() {
    private val songsList = MutableLiveData<ArrayList<Song>>()

    /*
    fun randomSong() {
        val currentSong: Song = SongProvider.random()
        songList.postValue(currentSong)
    }*/

    fun getSongsList(): LiveData<ArrayList<Song>> {
        val currentSongsList = SongProvider.getSongsList()
        songsList.postValue(currentSongsList)
        return songsList
    }
}