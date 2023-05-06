package com.example.musicpie2.model

import com.example.musicpie2.R

class SongProvider {
    companion object {
        fun random(): Song {
            val position = (songsList.indices).random()
            return songsList[position]
        }

        fun getSongsList(): ArrayList<Song> {
            return songsList
        }

        private val songsList = arrayListOf(
            Song(R.drawable.cover1, "Title 1", "Artist 1", R.raw.song1),
            Song(R.drawable.cover2, "Title 2", "Artist 2", R.raw.song2),
            Song(R.drawable.cover3, "Title 3", "Artist 3", R.raw.song3)
        )
    }
}