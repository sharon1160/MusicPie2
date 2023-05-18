package com.example.musicpie2.view.ui.player

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.musicpie2.databinding.FragmentPlayerBinding
import com.example.musicpie2.view.ui.theme.AppTheme
import com.example.musicpie2.viewmodel.PlayerViewModel

class PlayerFragment : Fragment() {

    private var _binding: FragmentPlayerBinding? = null
    private val binding get() = _binding!!

    private lateinit var playerViewModel: PlayerViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        playerViewModel = ViewModelProvider(this)[PlayerViewModel::class.java]
        arguments?.let { playerViewModel.setArguments(it) }
        playerViewModel.initializeVariables(this)
        playerViewModel.checkDestroyMediaPlayer()
        playerViewModel.checkMediaPlayer(requireContext())

        _binding = FragmentPlayerBinding.inflate(inflater, container, false).apply {
            composeView.setContent {
                _binding?.root?.let {
                    playerViewModel.setNavController(Navigation.findNavController(it))
                }
                AppTheme {
                    val uiState by playerViewModel.uiState.collectAsState()
                    PlayerScreen(
                        uiState = uiState,
                        onPlayPauseClick = {playerViewModel.playPauseOnClick(requireContext())},
                        onNextClick = {playerViewModel.onNextClick(requireContext())},
                        onPreviousClick = {playerViewModel.onPreviousClick(requireContext())},
                        navigationToSettings = {playerViewModel.navigationToSettings()},
                        navigationToBack = {playerViewModel.navigationToBack()}
                    )
                }
            }
        }
        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        playerViewModel.destroy()
    }
}
