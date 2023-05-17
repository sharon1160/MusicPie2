package com.example.musicpie2.view.ui

import android.media.MediaPlayer
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContentProviderCompat
import coil.compose.AsyncImagePainter.State.Empty.painter
import coil.compose.rememberAsyncImagePainter
import com.example.musicpie2.R
import com.example.musicpie2.model.MediaPlayerSingleton
import com.example.musicpie2.model.PlaylistSingleton
import com.example.musicpie2.view.ui.theme.NotoSerif

private val playlistSingleton = PlaylistSingleton
private val playlist = playlistSingleton.playlist

@Composable
fun PlayerScreen(
    uiState: PlayerUiState,
    onPlayPauseClick: () -> Unit,
    onNextClick: () -> Unit,
    onPreviousClick: () -> Unit,
    navigationToSettings: () -> Unit,
    navigationToBack: () -> Unit,
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Scaffold(
            content = {
                PlayerContent(
                    uiState = uiState,
                    onPlayPauseClick = onPlayPauseClick,
                    onNextClick = onNextClick,
                    onPreviousClick = onPreviousClick,
                    navigationToSettings = navigationToSettings,
                    navigationToBack = navigationToBack
                )
            }
        )
    }
}

@Composable
fun PlayerContent(
    uiState: PlayerUiState,
    onPlayPauseClick: () -> Unit,
    onNextClick: () -> Unit,
    onPreviousClick: () -> Unit,
    navigationToSettings: () -> Unit,
    navigationToBack: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 30.dp, bottom = 30.dp, end = 15.dp, start = 15.dp),
        verticalArrangement = Arrangement.SpaceAround
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            DefaultIconButton(
                painter = painterResource(id = R.drawable.back_icon),
                description = "Back button",
                tint = MaterialTheme.colorScheme.primary,
                onClick = navigationToBack,
            )
            DefaultIconButton(
                painter = painterResource(id = R.drawable.settings_icon),
                description = "Settings Icon",
                tint = MaterialTheme.colorScheme.primary,
                onClick = navigationToSettings,
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
        ) {
            Image(
                painter = rememberAsyncImagePainter(model = playlist[uiState.currentPosition].cover),
                contentDescription = null,
            )
        }
        Column(
            modifier = Modifier.padding(start = 15.dp, end = 15.dp)
        ) {
            Text(
                text = playlist[uiState.currentPosition].songTitle,
                style = MaterialTheme.typography.bodyMedium,
                fontFamily = NotoSerif,
                fontWeight = FontWeight.Bold,
            )
            Text(
                text = playlist[uiState.currentPosition].songArtist,
                style = MaterialTheme.typography.bodySmall,
                fontFamily = NotoSerif,
                fontWeight = FontWeight.Light
            )
        }
        SeekBar()
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 15.dp, end = 15.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            PreviousButton(onClick = onPreviousClick)
            PlayPauseButton(isPlaying = uiState.isPlaying, onClick = onPlayPauseClick)
            NextButton(onClick = onNextClick)
        }
    }
}

@Composable
fun PreviousButton(onClick: () -> Unit) {
    IconButton(onClick = {
        onClick()
    }) {
        Icon(
            painter = painterResource(id = R.drawable.previous_icon),
            contentDescription = "Previous Icon",
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .width(150.dp)
                .height(150.dp)
        )
    }
}

@Composable
fun NextButton(onClick: () -> Unit) {
    IconButton(onClick = {
        onClick()
    }) {
        Icon(
            painter = painterResource(id = R.drawable.next_icon),
            contentDescription = "Next Icon",
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .width(150.dp)
                .height(150.dp)
        )
    }
}

@Composable
private fun PlayPauseButton(isPlaying: Boolean, onClick: () -> Unit) {
    val contextForToast = LocalContext.current.applicationContext
    IconButton(onClick = {
        val message = if (isPlaying) "Pause" else "Play"
        Toast.makeText(contextForToast, message, Toast.LENGTH_SHORT).show()
        onClick()
    }) {
        val painter =
            if (isPlaying) {
                painterResource(id = R.drawable.pause_icon)
            } else {
                painterResource(id = R.drawable.play_icon)
            }
        Icon(
            painter = painter,
            contentDescription = "Play/Pause Icon",
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .width(150.dp)
                .height(150.dp)
        )
    }
}


@Composable
fun SeekBar() {
    var progress = 0f
    //val progress = remember { mutableStateOf(0f) }

    Column(
        modifier = Modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Slider(
            value = progress,
            onValueChange = { newValue ->
                progress = newValue
            },
            modifier = Modifier.width(500.dp),
            valueRange = 0f..100f,
            steps = 1,
            colors = SliderDefaults.colors(
                thumbColor = MaterialTheme.colorScheme.primary,
                activeTrackColor = MaterialTheme.colorScheme.primary
            )
        )
    }
}








