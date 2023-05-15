package com.example.musicpie2.view.ui

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import com.example.musicpie2.R
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.musicpie2.view.ui.theme.NotoSerif

@Preview
@Composable
fun HomeScreen() {
    Scaffold(
        topBar = { Toolbar(title = "Home") },
        content = { Content() }
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Toolbar(title: String) {
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
            TopAppBarActionButton(
                painter = painterResource(id = R.drawable.settings_icon),
                description = "Settings Icon",
                tint = MaterialTheme.colorScheme.onPrimary
            ) {
                Toast.makeText(contextForToast, "Lock Click", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    )
}

@Composable
fun TopAppBarActionButton(painter: Painter, description: String, tint: Color, onClick: () -> Unit) {
    IconButton(onClick = {
        onClick()
    }) {
        Icon(
            painter = painter,
            contentDescription = description,
            tint = tint
        )
    }
}

@Composable
private fun Content() {
    Column(
        modifier = Modifier.padding(top = 80.dp, start = 15.dp, end = 15.dp),
    ) {
        Text("hello")
        Image(
            painter = painterResource(id = R.drawable.cover1),
            contentDescription = null
        )
    }
}



