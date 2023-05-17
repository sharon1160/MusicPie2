package com.example.musicpie2.view.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.musicpie2.databinding.FragmentHomeBinding
import com.example.musicpie2.view.ui.theme.AppTheme
import com.example.musicpie2.viewmodel.HomeViewModel

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var homeViewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel = ViewModelProvider(this)[HomeViewModel::class.java]

        arguments?.let { homeViewModel.setArguments(it) }
        homeViewModel.initializeVariables()

        _binding = FragmentHomeBinding.inflate(inflater, container, false).apply {
            composeView.setContent {
                AppTheme {
                    _binding?.root?.let {
                        homeViewModel.setNavController(Navigation.findNavController(it))
                    }
                    val uiState by homeViewModel.uiState.collectAsState()
                    HomeScreen(
                        uiState = uiState,
                        onSettingsClick = { homeViewModel.navigationToSettings() },
                        onPlayPauseClick = { homeViewModel.playPauseOnClick() },
                        onRandomClick = { homeViewModel.randomOnClick() }
                    )
                }
            }
        }

        /*
        initializeVariables()

        homeViewModel = ViewModelProvider(this)[HomeViewModel::class.java]

        initializePlayListRecycler()
        updatePlayIcon()
        updateRandomIcon()
        player()
        navigationToSettings()*/

        return binding.root
    }

    /*
    private fun initializeVariables() {
        playListRecycler = binding.playListRecycler
        playPauseButton = binding.playPauseButton
        randomButton = binding.randomButton
        settingsButton = binding.toolbar.settingsButton
        currentPosition = arguments?.getInt("currentPosition") ?: baseSongIndex
        destroyMediaplayer = arguments?.getBoolean("destroyMediaplayer") ?: false
    }

    private fun initializePlayListRecycler() {
        playListRecycler = binding.playListRecycler
        playlist = homeViewModel.getSongsList()
        val songAdapter = SongAdapter(playlist, this)
        playListRecycler.layoutManager = LinearLayoutManager(requireContext())
        playListRecycler.adapter = songAdapter
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

    private fun player() {
        playPauseOnClick()
        randomOnClick()
    }

    private fun playPauseOnClick() {

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
    }

    private fun randomOnClick() {

        randomButton.setOnClickListener {
            isRandom = if (isRandom) {
                currentPosition = 0
                randomButton.setBackgroundResource(R.drawable.random_off_icon)
                Toast.makeText(requireContext(), "Shuffle not enabled", Toast.LENGTH_SHORT).show()
                false
            } else {
                currentPosition = (0 until playlist.size).random()
                randomButton.setBackgroundResource(R.drawable.random_on_icon)
                Toast.makeText(requireContext(), "Shuffle enabled", Toast.LENGTH_SHORT).show()
                true
            }
            isPlaying = false
            destroyMediaplayer = true
        }
    }


    private fun navigationToPlayer(view: View) {
        val bundle = Bundle()
        bundle.putBoolean("isPlaying", isPlaying)
        bundle.putBoolean("isRandom", isRandom)
        bundle.putBoolean("destroyMediaplayer", destroyMediaplayer)
        bundle.putInt("currentPosition", currentPosition)
        Navigation.findNavController(view)
            .navigate(R.id.action_homeFragment_to_playerFragment, bundle)
    }

    private fun navigationToSettings() {
        settingsButton.setOnClickListener { view: View ->
            mediaPlayerSingleton.pause()
            playPauseButton.setBackgroundResource(R.drawable.play_icon)
            Navigation.findNavController(view)
                .navigate(R.id.action_homeFragment_to_settingsFragment)
        }
        Toast.makeText(requireContext(), "Random Click", Toast.LENGTH_SHORT).show()
    }

    override fun onItemClick(item: Song, position: Int) {
        Toast.makeText(requireContext(), item.songTitle, Toast.LENGTH_LONG).show()
    }
     */
}
