package com.example.musicpie2.view.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.musicpie2.R
import com.example.musicpie2.databinding.FragmentHomeBinding
import com.example.musicpie2.model.Song
import com.example.musicpie2.view.adapter.SongAdapter
import com.example.musicpie2.viewmodel.SongViewModel


class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val songViewModel : SongViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        var songAdapter = SongAdapter(ArrayList())
        binding.playListRecycler.layoutManager = LinearLayoutManager(requireContext())
        binding.playListRecycler.adapter = songAdapter

        songViewModel.getSongsList().observe(viewLifecycleOwner, Observer {songsList ->
            songAdapter = SongAdapter(songsList)
            binding.playListRecycler.adapter = SongAdapter(songsList)
        })

        return binding.root
    }
}
