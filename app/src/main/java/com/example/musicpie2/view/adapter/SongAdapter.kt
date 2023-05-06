package com.example.musicpie2.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.musicpie2.R
import com.example.musicpie2.model.Song
import com.google.android.material.imageview.ShapeableImageView

class SongAdapter(private val songsList: ArrayList<Song>) :
    RecyclerView.Adapter<SongAdapter.SongViewHolder>() {

    class SongViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cover: ShapeableImageView = itemView.findViewById(R.id.cover)
        val songTitle: TextView = itemView.findViewById(R.id.songTitle)
        val songArtist: TextView = itemView.findViewById(R.id.songArtist)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.playlist_item, parent, false)
        return SongViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return songsList.size
    }

    override fun onBindViewHolder(holder: SongViewHolder, position: Int) {
        val currentSong = songsList[position]
        holder.cover.setImageResource(currentSong.cover)
        holder.songTitle.text = currentSong.songTitle
        holder.songArtist.text = currentSong.songArtist

    }
}
