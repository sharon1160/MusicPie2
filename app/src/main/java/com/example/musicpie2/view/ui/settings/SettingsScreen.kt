package com.example.musicpie2.view.ui.settings

import android.net.Uri
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.example.musicpie2.R
import com.example.musicpie2.model.Song
import com.example.musicpie2.model.SongFileContentProvider
import com.example.musicpie2.view.ui.home.DefaultIconButton
import com.example.musicpie2.view.ui.theme.NotoSerif

@Composable
fun SettingsScreen(onActionClick: (String) -> Unit, onBackClick: () -> Unit, allSongsList: List<Song>) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Scaffold(
            topBar = { SettingsToolbar(title = "Settings", onBackClick) },
            content = { Content(onActionClick, allSongsList) }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsToolbar(title: String, onBackClick: () -> Unit) {
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
        navigationIcon = {
            DefaultIconButton(
                painter = painterResource(id = R.drawable.back_icon),
                description = "Back Icon",
                tint = MaterialTheme.colorScheme.onPrimary
            ) {
                onBackClick()
            }
        },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primary
        )
    )
}

@Composable
private fun Content(onActionClick: (String) -> Unit, allSongsList: List<Song>) {
    Column(
        modifier = Modifier.padding(top = 80.dp, start = 15.dp, end = 15.dp),
    ) {
        PlaylistRecycler(onActionClick, allSongsList)
    }
}

@Composable
fun PlaylistRecycler(onActionClick: (String) -> Unit, songsList: List<Song>) {
    LazyColumn(modifier = Modifier.padding(vertical = 4.dp)) {
        items(items = songsList) { song ->
            SettingsListItem(
                song = song,
                onActionClick = onActionClick
            )
        }
    }
}

@Composable
fun SettingsListItem(song: Song, onActionClick: (String) -> Unit) {
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
                    painter = rememberAsyncImagePainter(model = song.cover),
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
                        text = song.songTitle,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold,
                        fontFamily = NotoSerif
                    )
                    Text(
                        text = song.songArtist,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Light,
                        fontFamily = NotoSerif
                    )
                }
                ActionButton(song, onActionClick)
            }
        }
    }
}

@Composable
fun ActionButton(song: Song, onActionClick: (String) -> Unit) {
    IconButton(onClick = {
        onActionClick(song.songTitle)
    }) {
        val painter =
            if (song.inPlaylist) {
                painterResource(id = R.drawable.delete_icon)
            } else {
                painterResource(id = R.drawable.add_icon)
            }
        Icon(
            painter = painter,
            contentDescription = "Action Icon",
            modifier = Modifier
                .width(40.dp)
                .height(40.dp),
            tint = MaterialTheme.colorScheme.primary
        )
    }
}


@Preview
@Composable
fun Test() {
    val allSongsList = arrayListOf(
        Song(
            Uri.parse("${SongFileContentProvider.URI_RSC}${R.drawable.cover1}"),
            "Chlorine",
            "Twenty One Pilots",
            Uri.parse("${SongFileContentProvider.URI_RSC}${R.raw.song1}"),
            true
        ),
        Song(
            Uri.parse("${SongFileContentProvider.URI_RSC}${R.drawable.cover2}"),
            "Ride",
            "Twenty One Pilots",
            Uri.parse("${SongFileContentProvider.URI_RSC}${R.raw.song2}"),
            true
        ),
        Song(
            Uri.parse("${SongFileContentProvider.URI_RSC}${R.drawable.cover3}"),
            "La La La",
            "Naughty Boy, Sam Smith",
            Uri.parse("${SongFileContentProvider.URI_RSC}${R.raw.song3}"),
            true
        ),
        Song(
            Uri.parse("${SongFileContentProvider.URI_RSC}${R.drawable.cover4}"),
            "Atlantis",
            "Seafret",
            Uri.parse("${SongFileContentProvider.URI_RSC}${R.raw.song4}"),
            false
        ),
        Song(
            Uri.parse("${SongFileContentProvider.URI_RSC}${R.drawable.cover5}"),
            "Everglow",
            "Coldplay",
            Uri.parse("${SongFileContentProvider.URI_RSC}${R.raw.song5}"),
            false
        ),
    )
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Scaffold(
            topBar = { SettingsToolbar(title = "Settings") {} },
            content = { Content({}, allSongsList) }
        )
    }
}
