package com.example.musicpie2.view.ui.home

import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import com.example.musicpie2.R
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.musicpie2.model.PlaylistSingleton
import com.example.musicpie2.model.Song
import com.example.musicpie2.view.ui.home.HomeUiState
import com.example.musicpie2.view.ui.theme.NotoSerif
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState

private val playlistSingleton = PlaylistSingleton

@Composable
fun HomeScreen(
    uiState: HomeUiState,
    onSettingsClick: () -> Unit,
    onPlayPauseClick: () -> Unit,
    onRandomClick: () -> Unit,
    updateData: () -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Scaffold(
            topBar = { Toolbar(title = "Home", onSettingsClick = onSettingsClick) },
            content = {
                Content(
                    uiState = uiState,
                    subtitle = "Playlist",
                    onPlayPauseClick = onPlayPauseClick,
                    onRandomClick = onRandomClick,
                    updateData = updateData
                )
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Toolbar(title: String, onSettingsClick: () -> Unit) {
    val contextForToast = LocalContext.current.applicationContext
    TopAppBar(
        title = {
            Text(
                text = title,
                color = MaterialTheme.colorScheme.onPrimary,
                fontFamily = NotoSerif,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.titleLarge,
            )
        },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary
        ),
        actions = {
            DefaultIconButton(
                painter = painterResource(id = R.drawable.settings_icon),
                description = "Settings Icon",
                tint = MaterialTheme.colorScheme.onPrimary
            ) {
                onSettingsClick()
            }
        }
    )
}

@Composable
fun DefaultIconButton(painter: Painter, description: String, tint: Color, onClick: () -> Unit) {
    IconButton(onClick = {
        onClick()
    }) {
        Icon(
            painter = painter,
            contentDescription = description,
            tint = tint,
            modifier = Modifier
                .width(30.dp)
                .height(30.dp)
        )
    }
}

@Composable
private fun Content(
    uiState: HomeUiState,
    subtitle: String,
    onPlayPauseClick: () -> Unit,
    onRandomClick: () -> Unit,
    updateData: () -> Unit
) {
    Column(
        modifier = Modifier.padding(top = 80.dp, start = 15.dp, end = 15.dp),
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 15.dp)
        ) {
            PlaylistTitle(subtitle = subtitle)
            PlayPauseButton(isPlaying = uiState.isPlaying, onPlayPauseClick = onPlayPauseClick)
            RandomButton(isRandom = uiState.isRandom, onRandomClick = onRandomClick)
        }
        SongsListRecycler(playlistSingleton.playlist, uiState.isRefreshing, updateData)
    }
}

@Composable
fun PlaylistTitle(subtitle: String) {
    Text(
        text = subtitle,
        color = MaterialTheme.colorScheme.onSurface,
        fontFamily = NotoSerif,
        fontWeight = FontWeight.Bold,
        style = MaterialTheme.typography.titleLarge,
        modifier = Modifier.padding(end = 10.dp),
    )
}

@Composable
private fun PlayPauseButton(isPlaying: Boolean, onPlayPauseClick: () -> Unit) {
    val contextForToast = LocalContext.current.applicationContext
    IconButton(onClick = {
        val message = if (isPlaying) "Pause" else "Play"
        Toast.makeText(contextForToast, message, Toast.LENGTH_SHORT).show()
        onPlayPauseClick()
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
                .width(40.dp)
                .height(40.dp)
        )
    }
}

@Composable
fun RandomButton(isRandom: Boolean, onRandomClick: () -> Unit) {
    val contextForToast = LocalContext.current.applicationContext
    IconButton(onClick = {
        val message = if (isRandom) "Random off" else "Random on"
        Toast.makeText(contextForToast, message, Toast.LENGTH_SHORT).show()
        onRandomClick()
    }) {
        var painter = painterResource(id = R.drawable.random_off_icon)
        var tint = MaterialTheme.colorScheme.outline
        if (isRandom) {
            painter = painterResource(id = R.drawable.random_on_icon)
            tint = MaterialTheme.colorScheme.primary
        } else {
            painter = painterResource(id = R.drawable.random_off_icon)
            tint = MaterialTheme.colorScheme.outline
        }

        Icon(
            painter = painter,
            contentDescription = "Random Icon",
            modifier = Modifier
                .width(40.dp)
                .height(40.dp),
            tint = tint
        )
    }
}

@Composable
fun SongsListRecycler(
    songsList: ArrayList<Song> = arrayListOf(),
    isRefreshing: Boolean,
    updateData: () -> Unit
) {
    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = isRefreshing)
    SwipeRefresh(state = swipeRefreshState, onRefresh = { updateData() }) {
        LazyColumn(modifier = Modifier.padding(vertical = 4.dp)) {
            items(items = songsList) { song ->
                ListItem(cover = song.cover, songTitle = song.songTitle, artist = song.songArtist)
            }
        }
    }
}

@Composable
fun ListItem(cover: Uri, songTitle: String, artist: String) {
    Surface(
        color = MaterialTheme.colorScheme.onPrimary,
        modifier = Modifier.padding(vertical = 0.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
        ) {
            val lineColor = MaterialTheme.colorScheme.inversePrimary
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .drawBehind {
                        drawLine(
                            color = lineColor,
                            start = Offset(0f, size.height),
                            end = Offset(size.width, size.height),
                            strokeWidth = 2.dp.toPx()
                        )
                    }
            ) {
                Image(
                    painter = rememberAsyncImagePainter(model = cover),
                    contentDescription = null,
                    modifier = Modifier
                        .padding(start = 0.dp, top = 6.dp, bottom = 6.dp)
                        .height(80.dp)
                        .width(80.dp)
                        .clip(MaterialTheme.shapes.medium),
                    contentScale = ContentScale.Crop
                )

                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 16.dp, end = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Text(
                        text = songTitle,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = artist,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Light
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun HomePreview() {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Scaffold(
            topBar = { Toolbar(title = "Home", onSettingsClick = {} ) },
            content = {
                Content(
                    uiState = HomeUiState(),
                    subtitle = "Playlist",
                    onPlayPauseClick = {} ,
                    onRandomClick = {} ,
                    updateData = {}
                )
            }
        )
    }
}
