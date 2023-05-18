package com.example.musicpie2.view.ui.home

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
                        onRandomClick = { homeViewModel.randomOnClick() },
                        updateData = {homeViewModel.updateData()}
                    )
                }
            }
        }
        return binding.root
    }
}
