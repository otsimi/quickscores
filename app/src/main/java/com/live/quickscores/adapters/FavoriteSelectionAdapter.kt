package com.live.quickscores.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.live.quickscores.FavoriteItem
import com.live.quickscores.R


class FavoriteSelectionAdapter( private val onFavoriteClick: (FavoriteItem) -> Unit): RecyclerView.Adapter<FavoriteSelectionAdapter.FavoriteSelectionViewHolder>() {
    private val items = mutableListOf<FavoriteItem>()

    @SuppressLint("NotifyDataSetChanged")

    fun submitList(newItems: List<FavoriteItem>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ):FavoriteSelectionViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.favorite_item, parent, false)
        return FavoriteSelectionViewHolder(view)

    }

    override fun onBindViewHolder(
        holder:FavoriteSelectionViewHolder,
        position: Int
    ) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class FavoriteSelectionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val imageLogo = itemView.findViewById<ImageView>(R.id.team_player_logo)
        private val title = itemView.findViewById<TextView>(R.id.team_player_name)
        private val subtitle = itemView.findViewById<TextView>(R.id.team_player_league_or_teamName)
        private val favoriteIcon = itemView.findViewById<ImageView>(R.id.favoriteIcon)

        fun bind(item: FavoriteItem) {
            when (item) {
                is FavoriteItem.TeamItem -> {
                    title.text = item.team.name
                    subtitle.text = item.team.country
                    Glide.with(imageLogo).load(item.team.logo).into(imageLogo)
                }

                is FavoriteItem.PlayerItem -> {
                    title.text = item.player.name
                    subtitle.text = item.player.nationality
                    Glide.with(imageLogo).load(item.player.photo).into(imageLogo)
                }
            }
            favoriteIcon.setOnClickListener {
                onFavoriteClick(item)


            }
        }
    }
}


