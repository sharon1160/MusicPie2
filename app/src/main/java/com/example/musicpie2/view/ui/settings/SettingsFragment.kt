package com.example.musicpie2.view.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.musicpie2.databinding.FragmentSettingsBinding
import com.example.musicpie2.view.ui.theme.AppTheme
import com.example.musicpie2.viewmodel.SettingsViewModel

class SettingsFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    private lateinit var settingsViewModel: SettingsViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        settingsViewModel = ViewModelProvider(this)[SettingsViewModel::class.java]
        settingsViewModel.loadAllSongsList(requireActivity().contentResolver)

        _binding = FragmentSettingsBinding.inflate(inflater, container, false).apply {
            composeView.setContent {
                AppTheme {
                    _binding?.root?.let {
                        settingsViewModel.setNavController(Navigation.findNavController(it))
                    }
                    val uiState by settingsViewModel.uiState.collectAsState()
                    SettingsScreen(
                        onActionClick = settingsViewModel::onActionClick,
                        onBackClick = {settingsViewModel.onBackClick()},
                        allSongsList = uiState.songsList
                    )
                }
            }
        }

        return binding.root
    }
}
