package com.example.musicpie2.model

import android.net.Uri
import android.os.Build
import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.RequiresApi

data class Song(
    val cover: Uri,
    val songTitle: String,
    val songArtist: String,
    val audio: Uri,
    var inPlaylist: Boolean
) :
    Parcelable {

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(cover, flags)
        parcel.writeString(songTitle)
        parcel.writeString(songArtist)
        parcel.writeParcelable(audio, flags)
        parcel.writeByte(if (inPlaylist) 1 else 0)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Song> {
        @RequiresApi(Build.VERSION_CODES.M)
        override fun createFromParcel(parcel: Parcel): Song {

            return Song(
                parcel.readTypedObject(Uri.CREATOR)!!,
                parcel.readString().toString(),
                parcel.readString().toString(),
                parcel.readTypedObject(Uri.CREATOR)!!,
                parcel.readByte() != 0.toByte())
        }

        override fun newArray(size: Int): Array<Song?> {
            return arrayOfNulls(size)
        }
    }
}
