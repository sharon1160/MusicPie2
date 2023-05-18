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
import androidx.recyclerview.widget.RecyclerView
import com.example.musicpie2.databinding.FragmentSettingsBinding
import com.example.musicpie2.model.MediaPlayerSingleton
import com.example.musicpie2.view.adapter.SettingsAdapter
import com.example.musicpie2.view.ui.theme.AppTheme
import com.example.musicpie2.viewmodel.SettingsViewModel


class SettingsFragment : Fragment() /*, DeleteSongClickListener, AddSongClickListener */ {


    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    private lateinit var settingsRecycler: RecyclerView
    private lateinit var settingsViewModel: SettingsViewModel
    private lateinit var songAdapter: SettingsAdapter
    private var mediaPlayerSingleton = MediaPlayerSingleton

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

        /*
        settingsViewModel = ViewModelProvider(this)[SettingsViewModel::class.java]
        settingsViewModel.loadAllSongsList(requireActivity().contentResolver)

        settingsRecycler = binding.playListSettingsRecycler
        songAdapter = SettingsAdapter(settingsViewModel.getAllSongsList(), this, this)

        settingsRecycler.layoutManager = LinearLayoutManager(requireContext())
        settingsRecycler.adapter = songAdapter*/

        return binding.root
    }
    /*
    override fun onDeleteSongClick(item: Song, position: Int) {
        onDestroy()
        settingsViewModel.removeSong(item, position)
        notifyAdapter(position, settingsViewModel.getAllSongsListSize() - 1)
        Toast.makeText(requireContext(), "Deleted song \"${item.songTitle}\" ", Toast.LENGTH_SHORT).show()
    }

    override fun onAddSongClick(item: Song, position: Int) {
        onDestroy()
        val lastPositionTrue = settingsViewModel.getLastTruePosition()
        settingsViewModel.addSong(item, position, lastPositionTrue)
        notifyAdapter(position, lastPositionTrue + 1)
        Toast.makeText(requireContext(), "Added song \"${item.songTitle}\" ", Toast.LENGTH_SHORT).show()
    }

    private fun notifyAdapter(deletedPosition: Int, addedPosition: Int) {
        songAdapter.notifyItemRemoved(deletedPosition)
        songAdapter.notifyItemInserted(addedPosition)
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayerSingleton.mediaPlayer?.stop()
        mediaPlayerSingleton.release()
    }*/
}
