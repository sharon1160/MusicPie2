package com.example.musicpie2.view.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import com.example.musicpie2.databinding.ActivityMainBinding
import com.example.musicpie2.viewmodel.HomeViewModel
import com.example.musicpie2.viewmodel.SettingsViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var settingsViewModel: SettingsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        homeViewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        homeViewModel.loadSongsList(contentResolver)

        Log.e("Error","${homeViewModel.getSongsList()}")
    }
}
