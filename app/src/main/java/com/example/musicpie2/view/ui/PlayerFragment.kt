package com.example.musicpie2.view.ui

import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.SeekBar
import androidx.constraintlayout.motion.widget.MotionLayout
import com.example.musicpie2.R
import android.os.Handler
import android.os.Message
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.bumptech.glide.Glide
import com.example.musicpie2.databinding.FragmentPlayerBinding
import com.example.musicpie2.model.MediaPlayerSingleton
import com.example.musicpie2.model.Song
import com.example.musicpie2.viewmodel.HomeViewModel

class PlayerFragment : Fragment() {

    private var _binding: FragmentPlayerBinding? = null
    private val binding get() = _binding!!

    private lateinit var playPauseButton: Button
    private lateinit var nextButton: Button
    private lateinit var previousButton: Button
    private lateinit var settingsButton: Button
    private lateinit var backButton: Button
    private lateinit var seekBar: SeekBar
    private lateinit var cover: ImageView
    private var isPlaying: Boolean = true
    private var isRandom: Boolean = false
    private var destroyMediaplayer: Boolean = false
    private var playlist: ArrayList<Song> = arrayListOf()
    private val baseSongIndex = 0
    private var pos = baseSongIndex

    private var mediaPlayerSingleton = MediaPlayerSingleton
    private var BROADCAST_ACTION = "com.example.musicpie2.view.ui.PLAYER_BROADCAST"

    private lateinit var homeViewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPlayerBinding.inflate(inflater, container, false)

        homeViewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        playlist = homeViewModel.getSongsList()

        initialize()

        if (destroyMediaplayer) {
            onDestroy()
            destroyMediaplayer = false
        }

        mediaPlayerSingleton.mediaPlayer?.let {
            if (it.isPlaying) {
                playPauseButton.setBackgroundResource(R.drawable.pause_icon)
            }
            initSeekbar()
        }

        if (isPlaying) {
            if (mediaPlayerSingleton.mediaPlayer == null) {
                mediaPlayerSingleton.init(requireContext(), playlist[pos].audio)
            }
            sendToastBroadcast("Playing \"${playlist[pos].songTitle}\"")
            mediaPlayerSingleton.start()
            playPauseButton.setBackgroundResource(R.drawable.pause_icon)
            initSeekbar()
        } else {
            mediaPlayerSingleton.pause()
            playPauseButton.setBackgroundResource(R.drawable.play_icon)
        }

        loadCover(playlist[pos].cover, cover)

        player(playlist[pos].audio)

        return binding.root
    }

    private fun loadCover(uri: Uri, view: ImageView) {
        context?.let {
            Glide.with(it)
                .load(uri)
                .into(view)
        }
    }

    private fun initialize() {
        playPauseButton = binding.playPauseButton
        nextButton = binding.nextButton
        previousButton = binding.previousButton
        settingsButton = binding.settingsButton
        backButton = binding.backButton
        seekBar = binding.seekBar
        cover = binding.cover

        isPlaying = arguments?.getBoolean("isPlaying") ?: true
        isRandom = arguments?.getBoolean("isRandom") ?: false
        destroyMediaplayer = arguments?.getBoolean("destroyMediaplayer") ?: false
        pos = arguments?.getInt("pos") ?: baseSongIndex
    }

    private fun navigationToSettings(view: View) {
        Navigation.findNavController(view)
            .navigate(R.id.action_playerFragment_to_settingsFragment)
    }

    private fun player(id: Uri) {
        playPauseButton.setOnClickListener {

            if (mediaPlayerSingleton.isPlaying()) {
                mediaPlayerSingleton.pause()
                playPauseButton.setBackgroundResource(R.drawable.play_icon)
                isPlaying = false
                Toast.makeText(requireContext(), "Pause", Toast.LENGTH_SHORT).show()
            } else {
                if (mediaPlayerSingleton.mediaPlayer == null) {
                    mediaPlayerSingleton.init(requireContext(), id)
                }
                mediaPlayerSingleton.start()
                //startAnimation(motionLayout)
                playPauseButton.setBackgroundResource(R.drawable.pause_icon)
                isPlaying = true
                initSeekbar()
                sendToastBroadcast("Playing \"${playlist[pos].songTitle}\"")
            }
        }

        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, progress: Int, user: Boolean) {
                if (user) mediaPlayerSingleton.mediaPlayer?.seekTo(progress)
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
            }

        })

        nextButton.setOnClickListener {
            mediaPlayerSingleton.mediaPlayer?.pause()

            if (pos < playlist.size - 1) {
                pos++
            } else {
                pos = 0
            }

            mediaPlayerSingleton.mediaPlayer =
                MediaPlayer.create(requireContext(), playlist[pos].audio)
            mediaPlayerSingleton.mediaPlayer?.start()
            sendToastBroadcast("Playing \"${playlist[pos].songTitle}\"")
            playPauseButton.setBackgroundResource(R.drawable.pause_icon)
            isPlaying = true
            loadCover(playlist[pos].cover,cover)
        }

        previousButton.setOnClickListener {
            mediaPlayerSingleton.mediaPlayer?.pause()

            if (pos > 0) {
                pos--
            } else {
                pos = playlist.size - 1
            }

            mediaPlayerSingleton.mediaPlayer =
                MediaPlayer.create(requireContext(), playlist[pos].audio)
            mediaPlayerSingleton.mediaPlayer?.start()
            //startAnimation(motionLayout)
            playPauseButton.setBackgroundResource(R.drawable.pause_icon)
            isPlaying = true
            loadCover(playlist[pos].cover,cover)
        }

        settingsButton.setOnClickListener { view: View ->
            mediaPlayerSingleton.pause()
            playPauseButton.setBackgroundResource(R.drawable.play_icon)
            navigationToSettings(view)
        }

        backButton.setOnClickListener { view: View ->
            val bundle = Bundle()
            bundle.putBoolean("isPlaying", isPlaying)
            bundle.putBoolean("isRandom", isRandom)
            bundle.putBoolean("destroyMediaplayer", destroyMediaplayer)
            bundle.putInt("pos", pos)
            Navigation.findNavController(view)
                .navigate(R.id.action_playerFragment_to_homeFragment, bundle)
        }
    }

    private fun sendToastBroadcast(message: String) {
        val intent = Intent()
        intent.action = BROADCAST_ACTION
        intent.putExtra("message",message)
        requireContext().sendBroadcast(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        mediaPlayerSingleton.mediaPlayer?.stop()
        mediaPlayerSingleton.release()
    }

    private fun initSeekbar() {

        seekBar.max = mediaPlayerSingleton.mediaPlayer?.duration ?: 0
        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed(object : Runnable {
            override fun run() {
                try {
                    seekBar.progress = mediaPlayerSingleton.mediaPlayer!!.currentPosition
                    handler.postDelayed(this, 1000)
                } catch (e: Exception) {
                    seekBar.progress = 0
                }
            }
        }, 0)
    }
}