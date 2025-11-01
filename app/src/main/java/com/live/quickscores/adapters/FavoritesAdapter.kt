package com.live.quickscores.adapters

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.live.quickscores.DataClassFavorite
import com.live.quickscores.databinding.MatchesBinding

class FavoritesAdapter(
    private var favorites: List<DataClassFavorite> = emptyList(),
    private val onItemClick: ((DataClassFavorite) -> Unit)? = null
) : RecyclerView.Adapter<FavoritesAdapter.FavoriteViewHolder>() {

    inner class FavoriteViewHolder(val binding: MatchesBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val binding = MatchesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        binding.favoriteIcon.visibility = View.GONE
        return FavoriteViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        val item = favorites[position]

        with(holder.binding) {
            HomeTeam.text = item.homeTeam
            AwayTeam.text = item.awayTeam
            HomeGoals.text = item.homeGoals
            AwayGoals.text = item.awayGoals
            Time.text = formattedTime(item.time)
            Log.d("formatted time","time formatted")
            println("Formatted time: ${formattedTime(item.time)}")

            Glide.with(root.context)
                .load(item.homeLogo)
                .into(HomeLogo)

            Glide.with(root.context)
                .load(item.awayLogo)
                .into(AwayLogo)

            root.setOnClickListener {
                onItemClick?.invoke(item)
            }
        }
    }

    override fun getItemCount(): Int = favorites.size

    @SuppressLint("NotifyDataSetChanged")
    fun updateList(newList: List<DataClassFavorite>) {
        favorites = newList
        notifyDataSetChanged()
    }

    @SuppressLint("NewApi")
    private fun formattedTime(isoTime: String): String {
        return try {
            val offsetDateTime = java.time.OffsetDateTime.parse(isoTime)
            val zonedDateTime = offsetDateTime.atZoneSameInstant(java.time.ZoneId.systemDefault())
            java.time.format.DateTimeFormatter.ofPattern("HH:mm", java.util.Locale.getDefault()).format(zonedDateTime)
        } catch (e: Exception) {
            "N/A"
        }
    }
}
