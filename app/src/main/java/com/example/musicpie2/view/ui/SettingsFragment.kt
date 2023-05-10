package com.example.musicpie2.view.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.musicpie2.databinding.FragmentSettingsBinding
import com.example.musicpie2.model.MediaPlayerSingleton
import com.example.musicpie2.model.Song
import com.example.musicpie2.view.adapter.AddSongClickListener
import com.example.musicpie2.view.adapter.DeleteSongClickListener
import com.example.musicpie2.view.adapter.SettingsAdapter
import com.example.musicpie2.viewmodel.SettingsViewModel


class SettingsFragment : Fragment(), DeleteSongClickListener, AddSongClickListener {

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
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)

        settingsViewModel = ViewModelProvider(this)[SettingsViewModel::class.java]
        settingsViewModel.loadAllSongsList(requireActivity().contentResolver)

        settingsRecycler = binding.playListSettingsRecycler
        songAdapter = SettingsAdapter(settingsViewModel.getAllSongsList(), this, this)

        settingsRecycler.layoutManager = LinearLayoutManager(requireContext())
        settingsRecycler.adapter = songAdapter

        return binding.root
    }

    override fun onDeleteSongClick(item: Song, position: Int) {
        onDestroy()
        settingsViewModel.removeSong(item, position)
        songAdapter.notifyItemRemoved(position)
        songAdapter.notifyItemInserted(settingsViewModel.getAllSongsListSize() - 1)
        Toast.makeText(requireContext(), "Deleted song \"${item.songTitle}\" ", Toast.LENGTH_SHORT).show()
    }

    override fun onAddSongClick(item: Song, position: Int) {
        onDestroy()
        val lastPositionTrue = settingsViewModel.getLastTruePosition()
        settingsViewModel.addSong(item, position, lastPositionTrue)
        songAdapter.notifyItemRemoved(position)
        songAdapter.notifyItemInserted(lastPositionTrue + 1)
        Toast.makeText(requireContext(), "Added song \"${item.songTitle}\" ", Toast.LENGTH_SHORT).show()
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayerSingleton.mediaPlayer?.stop()
        mediaPlayerSingleton.release()
    }
}
