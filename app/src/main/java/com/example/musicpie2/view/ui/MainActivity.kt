package com.example.musicpie2.view.ui

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.musicpie2.databinding.ActivityMainBinding
import com.example.musicpie2.viewmodel.HomeViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var homeViewModel: HomeViewModel

    private var BROADCAST_ACTION = "com.example.musicpie2.view.ui.PLAYER_BROADCAST"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        registerBroadcast(applicationContext)

        homeViewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        homeViewModel.loadSongsList(contentResolver)

    }

    private fun registerBroadcast(context: Context) {
        val filter = IntentFilter(BROADCAST_ACTION)
        context.registerReceiver(BroadcastApp(), filter)
    }

    inner class BroadcastApp: BroadcastReceiver() {
        override fun onReceive(p0: Context?, intent: Intent?) {
            val message = intent?.getStringExtra("message") ?: "Playing"
            Toast.makeText(applicationContext, message, Toast.LENGTH_SHORT).show()
        }
    }
}
