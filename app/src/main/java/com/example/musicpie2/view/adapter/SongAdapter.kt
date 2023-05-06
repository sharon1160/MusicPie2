package com.example.musicpie2.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.musicpie2.R
import com.example.musicpie2.model.Song
import com.google.android.material.imageview.ShapeableImageView

class SongAdapter(private val songsList: ArrayList<Song>, private var clickListener: OnItemListClickListener) :
    RecyclerView.Adapter<SongViewHolder>() {

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
        holder.initialize(currentSong, clickListener)
    }
}
class SongViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val cover: ShapeableImageView = itemView.findViewById(R.id.cover)
    private val songTitle: TextView = itemView.findViewById(R.id.songTitle)
    private val songArtist: TextView = itemView.findViewById(R.id.songArtist)

    fun initialize (item: Song, action: OnItemListClickListener) {
        cover.setImageResource(item.cover)
        songTitle.text = item.songTitle
        songArtist.text = item.songArtist

        itemView.setOnClickListener {
            action.onItemClick(item,adapterPosition)
        }
    }
}
interface OnItemListClickListener {
    fun onItemClick(item: Song, position: Int)
}
