package com.example.musicpie2.view.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.musicpie2.R
import com.example.musicpie2.databinding.FragmentHomeBinding
import com.example.musicpie2.model.MediaPlayerSingleton
import com.example.musicpie2.model.Song
import com.example.musicpie2.view.adapter.OnItemListClickListener
import com.example.musicpie2.view.adapter.SongAdapter
import com.example.musicpie2.viewmodel.HomeViewModel

class HomeFragment : Fragment(), OnItemListClickListener {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var playlist: ArrayList<Song>
    private lateinit var playListRecycler: RecyclerView
    private lateinit var settingsButton: Button
    private lateinit var playPauseButton: Button
    private lateinit var randomButton: Button
    private var isRandom: Boolean = false
    private var isPlaying: Boolean = false
    private var destroyMediaplayer: Boolean = false
    private val baseSongIndex = 0
    private var pos = baseSongIndex
    private var mediaPlayerSingleton = MediaPlayerSingleton

    private lateinit var homeViewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        initialize()

        homeViewModel = ViewModelProvider(this)[HomeViewModel::class.java]

        playListRecycler = binding.playListRecycler
        playlist = homeViewModel.getSongsList()
        val songAdapter = SongAdapter(playlist, this)
        playListRecycler.layoutManager = LinearLayoutManager(requireContext())
        playListRecycler.adapter = songAdapter

        updatePlayIcon()
        updateRandomIcon()

        player()
        settings()
        return binding.root
    }

    override fun onItemClick(item: Song, position: Int) {
        Toast.makeText(requireContext(), item.songTitle, Toast.LENGTH_LONG).show()
    }

    private fun updatePlayIcon() {
        isPlaying = arguments?.getBoolean("isPlaying") ?: false
        if (isPlaying) {
            playPauseButton.setBackgroundResource(R.drawable.pause_icon)
        } else {
            playPauseButton.setBackgroundResource(R.drawable.play_icon)
        }
    }

    private fun updateRandomIcon() {
        isRandom = arguments?.getBoolean("isRandom") ?: false
        if (isRandom) {
            randomButton.setBackgroundResource(R.drawable.random_on_icon)
        } else {
            randomButton.setBackgroundResource(R.drawable.random_off_icon)
        }
    }

    private fun initialize() {
        playListRecycler = binding.playListRecycler
        playPauseButton = binding.playPauseButton
        randomButton = binding.randomButton
        settingsButton = binding.toolbar.settingsButton
        pos = arguments?.getInt("pos") ?: baseSongIndex
        destroyMediaplayer = arguments?.getBoolean("destroyMediaplayer") ?: false
    }

    private fun navigationToPlayer(view: View) {
        val bundle = Bundle()
        bundle.putBoolean("isPlaying", isPlaying)
        bundle.putBoolean("isRandom", isRandom)
        bundle.putBoolean("destroyMediaplayer", destroyMediaplayer)
        bundle.putInt("pos", pos)
        Navigation.findNavController(view)
            .navigate(R.id.action_homeFragment_to_playerFragment, bundle)
    }

    private fun navigationToSettings(view: View) {
        Navigation.findNavController(view)
            .navigate(R.id.action_homeFragment_to_settingsFragment)
    }

    private fun player() {
        playPauseButton.setOnClickListener { view: View ->
            isPlaying = if (isPlaying) {
                Toast.makeText(requireContext(), "Pause", Toast.LENGTH_SHORT).show()
                playPauseButton.setBackgroundResource(R.drawable.play_icon)
                false
            } else {
                playPauseButton.setBackgroundResource(R.drawable.pause_icon)
                true
            }
            randomButton.setBackgroundResource(R.drawable.random_off_icon)
            isRandom = false
            navigationToPlayer(view)
        }

        randomButton.setOnClickListener {
            isRandom = if (isRandom) {
                pos = 0
                randomButton.setBackgroundResource(R.drawable.random_off_icon)
                Toast.makeText(requireContext(), "Shuffle not enabled", Toast.LENGTH_SHORT).show()
                false
            } else {
                pos = (0 until playlist.size).random()
                randomButton.setBackgroundResource(R.drawable.random_on_icon)
                Toast.makeText(requireContext(), "Shuffle enabled", Toast.LENGTH_SHORT).show()
                true
            }
            isPlaying = false
            destroyMediaplayer = true
        }
    }

    private fun settings() {
        settingsButton.setOnClickListener { view: View ->
            mediaPlayerSingleton.pause()
            playPauseButton.setBackgroundResource(R.drawable.play_icon)
            navigationToSettings(view)
        }
    }
}
