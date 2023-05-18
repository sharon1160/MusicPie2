package com.example.musicpie2.view.ui.player

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.musicpie2.databinding.FragmentPlayerBinding
import com.example.musicpie2.model.MediaPlayerSingleton
import com.example.musicpie2.model.Song
import com.example.musicpie2.view.ui.player.PlayerScreen
import com.example.musicpie2.view.ui.theme.AppTheme
import com.example.musicpie2.viewmodel.PlayerViewModel

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
    private lateinit var songTitle: TextView
    private lateinit var songArtist: TextView
    private var isPlaying: Boolean = true
    private var isRandom: Boolean = false
    private var destroyMediaplayer: Boolean = false
    private var playlist: ArrayList<Song> = arrayListOf()
    private val baseSongIndex = 0
    private var currentPosition = baseSongIndex

    private var mediaPlayerSingleton = MediaPlayerSingleton
    private var BROADCAST_ACTION = "com.example.musicpie2.view.ui.PLAYER_BROADCAST"

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

        /*
        homeViewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        playlist = homeViewModel.getSongsList()


        initializeVariables()
        checkDestroyMediaPlayer()
        checkMediaPlayer()
        player(playlist[currentPosition].audio)*/

        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        playerViewModel.destroy()
    }
    /*
    private fun initializeVariables() {
        playPauseButton = binding.playPauseButton
        nextButton = binding.nextButton
        previousButton = binding.previousButton
        settingsButton = binding.settingsButton
        backButton = binding.backButton
        seekBar = binding.seekBar
        cover = binding.cover
        songTitle = binding.title
        songArtist = binding.artist


        isPlaying = arguments?.getBoolean("isPlaying") ?: true
        isRandom = arguments?.getBoolean("isRandom") ?: false
        destroyMediaplayer = arguments?.getBoolean("destroyMediaplayer") ?: false
        currentPosition = arguments?.getInt("currentPosition") ?: baseSongIndex
    }

    private fun checkDestroyMediaPlayer() {
        if (destroyMediaplayer) {
            onDestroy()
            destroyMediaplayer = false
        }
    }

    private fun checkMediaPlayer() {
        mediaPlayerSingleton.mediaPlayer?.let {
            if (it.isPlaying) {
                playPauseButton.setBackgroundResource(R.drawable.pause_icon)
            }
            initSeekbar()
        }

        if (isPlaying) {
            if (mediaPlayerSingleton.mediaPlayer == null) {
                mediaPlayerSingleton.init(requireContext(), playlist[currentPosition].audio)
            }
            sendToastBroadcast("Playing \"${playlist[currentPosition].songTitle}\"")
            mediaPlayerSingleton.start()
            playPauseButton.setBackgroundResource(R.drawable.pause_icon)
            initSeekbar()
        } else {
            mediaPlayerSingleton.pause()
            playPauseButton.setBackgroundResource(R.drawable.play_icon)
        }
        loadCover(playlist[currentPosition].cover, cover)
        songTitle.text = playlist[currentPosition].songTitle
        songArtist.text = playlist[currentPosition].songArtist
    }

    private fun loadCover(uri: Uri, view: ImageView) {
        context?.let {
            Glide.with(it)
                .load(uri)
                .into(view)
        }
    }

    private fun player(id: Uri) {
        playPauseOnClick(id)
        seekBarChangeListener()
        nextOnClick()
        previousOnClick()
        settingsOnClick()
        backOnClick()
    }

    private fun playPauseOnClick(id: Uri) {
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
                playPauseButton.setBackgroundResource(R.drawable.pause_icon)
                isPlaying = true
                initSeekbar()
                sendToastBroadcast("Playing \"${playlist[currentPosition].songTitle}\"")
            }
        }
    }

    private fun seekBarChangeListener() {
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, progress: Int, user: Boolean) {
                if (user) mediaPlayerSingleton.mediaPlayer?.seekTo(progress)
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
            }

        })
    }

    private fun nextOnClick() {
        nextButton.setOnClickListener {
            mediaPlayerSingleton.mediaPlayer?.pause()

            if (currentPosition < playlist.size - 1) {
                currentPosition++
            } else {
                currentPosition = 0
            }

            mediaPlayerSingleton.mediaPlayer =
                MediaPlayer.create(requireContext(), playlist[currentPosition].audio)
            mediaPlayerSingleton.mediaPlayer?.start()
            sendToastBroadcast("Playing \"${playlist[currentPosition].songTitle}\"")
            playPauseButton.setBackgroundResource(R.drawable.pause_icon)
            isPlaying = true
            loadCover(playlist[currentPosition].cover, cover)
            songTitle.text = playlist[currentPosition].songTitle
            songArtist.text = playlist[currentPosition].songArtist
        }
    }

    private fun previousOnClick() {
        previousButton.setOnClickListener {
            mediaPlayerSingleton.mediaPlayer?.pause()

            if (currentPosition > 0) {
                currentPosition--
            } else {
                currentPosition = playlist.size - 1
            }
            mediaPlayerSingleton.mediaPlayer =
                MediaPlayer.create(requireContext(), playlist[currentPosition].audio)
            mediaPlayerSingleton.mediaPlayer?.start()
            playPauseButton.setBackgroundResource(R.drawable.pause_icon)
            isPlaying = true
            loadCover(playlist[currentPosition].cover, cover)
            songTitle.text = playlist[currentPosition].songTitle
            songArtist.text = playlist[currentPosition].songArtist
        }
    }

    private fun settingsOnClick() {
        settingsButton.setOnClickListener { view: View ->
            mediaPlayerSingleton.pause()
            playPauseButton.setBackgroundResource(R.drawable.play_icon)
            navigationToSettings(view)
        }
    }

    private fun backOnClick(){
        backButton.setOnClickListener { view: View ->
            val bundle = Bundle()
            bundle.putBoolean("isPlaying", isPlaying)
            bundle.putBoolean("isRandom", isRandom)
            bundle.putBoolean("destroyMediaplayer", destroyMediaplayer)
            bundle.putInt("currentPosition", currentPosition)
            Navigation.findNavController(view)
                .navigate(R.id.action_playerFragment_to_homeFragment, bundle)
        }
    }

    private fun navigationToSettings(view: View) {
        Navigation.findNavController(view)
            .navigate(R.id.action_playerFragment_to_settingsFragment)
    }

    private fun sendToastBroadcast(message: String) {
        val intent = Intent()
        intent.action = BROADCAST_ACTION
        intent.putExtra("message", message)
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
    }*/
}
