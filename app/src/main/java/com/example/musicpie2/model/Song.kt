package com.example.musicpie2.model

import android.os.Parcel
import android.os.Parcelable

data class Song(val cover: Int, val songTitle: String, val songArtist: String, val audio: Int) :
    Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString().toString(),
        parcel.readString().toString(),
        parcel.readInt()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(cover)
        parcel.writeString(songTitle)
        parcel.writeString(songArtist)
        parcel.writeInt(audio)
    }
    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Song> {
        override fun createFromParcel(parcel: Parcel): Song {
            return Song(parcel)
        }
        override fun newArray(size: Int): Array<Song?> {
            return arrayOfNulls(size)
        }
    }
}
