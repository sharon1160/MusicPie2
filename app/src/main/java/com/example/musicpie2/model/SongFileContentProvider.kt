package com.example.musicpie2.model

import android.content.ContentProvider
import android.content.ContentValues
import android.database.Cursor
import android.database.MatrixCursor
import android.net.Uri
import com.example.musicpie2.R

class SongFileContentProvider : ContentProvider() {

    private var playlist = arrayListOf(
        Song(
            Uri.parse("$URI_RSC${R.drawable.cover1}"),
            "Chlorine",
            "Twenty One Pilots",
            Uri.parse("$URI_RSC${R.raw.song1}"),
            true
        ),
        Song(
            Uri.parse("$URI_RSC${R.drawable.cover2}"),
            "Ride",
            "Twenty One Pilots",
            Uri.parse("$URI_RSC${R.raw.song2}"),
            true
        ),
        Song(
            Uri.parse("$URI_RSC${R.drawable.cover3}"),
            "La La La",
            "Naughty Boy, Sam Smith",
            Uri.parse("$URI_RSC${R.raw.song3}"),
            true
        ),


        Song(
            Uri.parse("$URI_RSC${R.drawable.cover4}"),
            "Atlantis",
            "Seafret",
            Uri.parse("$URI_RSC${R.raw.song4}"),
            false
        ),
        Song(
            Uri.parse("$URI_RSC${R.drawable.cover5}"),
            "Everglow",
            "Coldplay",
            Uri.parse("$URI_RSC${R.raw.song5}"),
            false
        ),
        Song(
            Uri.parse("$URI_RSC${R.drawable.cover6}"),
            "Secrets",
            "OneRepublic",
            Uri.parse("$URI_RSC${R.raw.song6}"),
            false
        ),
        Song(
            Uri.parse("$URI_RSC${R.drawable.cover7}"),
            "On Melancholy Hill",
            "Gorillaz",
            Uri.parse("$URI_RSC${R.raw.song7}"),
            false
        ),
        Song(
            Uri.parse("$URI_RSC${R.drawable.cover8}"),
            "Moral of the Story",
            "Ashe",
            Uri.parse("$URI_RSC${R.raw.song8}"),
            false
        ),
        Song(
            Uri.parse("$URI_RSC${R.drawable.cover9}"),
            "Like a Stone",
            "Audioslave",
            Uri.parse("$URI_RSC${R.raw.song9}"),
            false
        ),
        Song(
            Uri.parse("$URI_RSC${R.drawable.cover10}"),
            "By the Way",
            "Red Hot Chili Peppers",
            Uri.parse("$URI_RSC${R.raw.song10}"),
            false
        )
    )

    companion object {
        private const val PROVIDER_NAME = "com.example.musicpie2.model/SongFileContentProvider"
        private const val PLAYLIST_PATH = "playlist"
        val CONTENT_URI: Uri = Uri.parse("content://$PROVIDER_NAME/$PLAYLIST_PATH")

        private const val PACKAGE_NAME = "com.example.musicpie2"
        const val URI_RSC = "android.resource://$PACKAGE_NAME/"

        const val COVER = "COVER"
        const val TITLE = "TITLE"
        const val ARTIST = "ARTIST"
        const val PATH = "PATH"
        const val IN_PLAYLIST = "IN_PLAYLIST"
    }

    override fun onCreate(): Boolean {
        return true
    }

    override fun query(
        uri: Uri,
        projection: Array<out String>?,
        selection: String?,
        selectionArgs: Array<out String>?,
        sortOrder: String?
    ): Cursor {
        val cursor = MatrixCursor(arrayOf(COVER, TITLE, ARTIST, PATH, IN_PLAYLIST))
        playlist.forEach {
            cursor.addRow(arrayOf(it.cover, it.songTitle, it.songArtist, it.audio, it.inPlaylist))
        }
        return cursor
    }

    fun getPlaylist(cursor: Cursor): ArrayList<Song> {
        val songsList = arrayListOf<Song>()

        cursor.let {
            val coverColumn = cursor.getColumnIndex(COVER)
            val titleColumn = cursor.getColumnIndex(TITLE)
            val artistColumn = cursor.getColumnIndex(ARTIST)
            val pathColumn = cursor.getColumnIndex(PATH)
            val inPlaylistColumn = cursor.getColumnIndex(IN_PLAYLIST)

            if (coverColumn == -1 || titleColumn == -1 || artistColumn == -1 || pathColumn == -1 || inPlaylistColumn == -1) {
                return arrayListOf()
            }

            if (cursor.moveToFirst()) {
                do {
                    val song = Song(
                        Uri.parse(cursor.getString(coverColumn)),
                        cursor.getString(titleColumn),
                        cursor.getString(artistColumn),
                        Uri.parse(cursor.getString(pathColumn)),
                        cursor.getString(inPlaylistColumn).toBoolean()
                    )
                    songsList.add(song)
                } while (cursor.moveToNext())
            }
        }
        return songsList
    }

    override fun getType(p0: Uri): String? {
        throw UnsupportedOperationException("Not supported")
    }

    override fun insert(p0: Uri, p1: ContentValues?): Uri? {
        throw UnsupportedOperationException("Not supported")
    }

    override fun delete(p0: Uri, p1: String?, p2: Array<out String>?): Int {
        throw UnsupportedOperationException("Not supported")
    }

    override fun update(p0: Uri, p1: ContentValues?, p2: String?, p3: Array<out String>?): Int {
        throw UnsupportedOperationException("Not supported")
    }
}