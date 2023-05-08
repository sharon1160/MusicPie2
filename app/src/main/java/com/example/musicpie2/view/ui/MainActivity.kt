package com.example.musicpie2.view.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.musicpie2.R
import com.example.musicpie2.databinding.ActivityMainBinding
import com.example.musicpie2.model.PlaylistSingleton
import com.example.musicpie2.model.Song

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        loadPlaylist()
    }

    private fun loadPlaylist() {
        PlaylistSingleton.playlist.add(Song(R.drawable.cover1, "Title 1", "Artist 1", R.raw.song1))
        PlaylistSingleton.playlist.add(Song(R.drawable.cover2, "Title 2", "Artist 2", R.raw.song2))
        PlaylistSingleton.playlist.add(Song(R.drawable.cover3, "Title 3", "Artist 3", R.raw.song3))
    }
}
