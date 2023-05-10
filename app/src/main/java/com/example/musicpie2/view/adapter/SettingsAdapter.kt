package com.example.musicpie2.view.adapter

import android.graphics.Color
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.musicpie2.R
import com.example.musicpie2.model.Song
import com.google.android.material.imageview.ShapeableImageView

class SettingsAdapter(
    var songsList: ArrayList<Song>,
    private var clickListenerDelete: DeleteSongClickListener,
    private var clickListenerAdd: AddSongClickListener
) :
    RecyclerView.Adapter<SongSettingViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SongSettingViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.settings_item, parent, false)
        return SongSettingViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return songsList.size
    }

    override fun onBindViewHolder(holder: SongSettingViewHolder, position: Int) {
        val currentItem = songsList[position]
        holder.initialize(currentItem, clickListenerDelete, clickListenerAdd)
    }
}

class SongSettingViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    val cover: ShapeableImageView = itemView.findViewById(R.id.cover)
    val songTitle: TextView = itemView.findViewById(R.id.songTitle)
    val songArtist: TextView = itemView.findViewById(R.id.songArtist)
    val actionButton: Button = itemView.findViewById(R.id.action_button)

    fun initialize(item: Song, actionDelete: DeleteSongClickListener, actionAdd: AddSongClickListener) {
        loadCover(item.cover,cover)
        songTitle.text = item.songTitle
        songArtist.text = item.songArtist

        if (item.inPlaylist) {
            actionButton.setBackgroundResource(R.drawable.delete_icon)
        }
        else {
            actionButton.setBackgroundResource(R.drawable.add_song_icon)
        }

        actionButton.setOnClickListener {
            if (item.inPlaylist) {
                actionDelete.onDeleteSongClick(item, adapterPosition)
            }
            else {
                actionAdd.onAddSongClick(item, adapterPosition)
            }
        }
    }

    private fun loadCover(uri: Uri, view: ImageView) {
        itemView?.let {
            Glide.with(itemView)
                .load(uri)
                .into(view)
        }
    }
}

interface AddSongClickListener {
    fun onAddSongClick(item: Song, position: Int)
}

interface DeleteSongClickListener {
    fun onDeleteSongClick(item: Song, position: Int)
}
