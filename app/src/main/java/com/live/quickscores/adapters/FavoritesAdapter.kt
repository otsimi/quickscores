package com.live.quickscores.adapters

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.live.quickscores.FavoriteFixtureEntity
import com.live.quickscores.R
import com.live.quickscores.databinding.MatchesBinding
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

class FavoritesAdapter(
    private var favorites: List<FavoriteFixtureEntity> = emptyList(),
    private val onItemClick: ((FavoriteFixtureEntity) -> Unit)? = null
) : RecyclerView.Adapter<FavoritesAdapter.FavoriteViewHolder>() {

    inner class FavoriteViewHolder(val binding: MatchesBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val binding = MatchesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        // Initial visibility setup
        binding.favoriteIcon.visibility = View.GONE
        binding.HomeGoals.visibility = View.VISIBLE
        binding.AwayGoals.visibility = View.VISIBLE
        binding.cancelled.visibility = View.GONE
        return FavoriteViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        val item = favorites[position]

        with(holder.binding) {
            // Set team names and logos
            HomeTeam.text = item.homeTeam
            AwayTeam.text = item.awayTeam
            Glide.with(root.context).load(item.homeLogo).into(HomeLogo)
            Glide.with(root.context).load(item.awayLogo).into(AwayLogo)

            val fixtureStatus = item.fixtureStatus
            val matchPeriod = item.matchPeriod ?: ""
            val formattedTime = formatIsoTime(item.time)

            // Set Time and Goals based on status
            when {
                fixtureStatus in listOf("1H", "2H", "ET") -> {
                    Time.text = "${matchPeriod}'"
                    Time.setTextColor(ContextCompat.getColor(root.context, R.color.orange_red))
                    setGoals(HomeGoals, AwayGoals, item.homeGoals, item.awayGoals)
                    cancelled.visibility = View.GONE
                }

                fixtureStatus == "INT" -> {
                    Time.text = "INT (${matchPeriod}')"
                    setGoals(HomeGoals, AwayGoals, item.homeGoals, item.awayGoals)
                }

                fixtureStatus in listOf("HT", "FT", "AET", "PEN") &&
                        ((item.homeGoals?.toIntOrNull() ?: 0) > 0 || (item.awayGoals?.toIntOrNull() ?: 0) > 0) -> {
                    Time.text = fixtureStatus
                    setGoals(HomeGoals, AwayGoals, item.homeGoals, item.awayGoals)
                    cancelled.visibility = View.GONE
                }

                fixtureStatus in listOf("PST", "CANC", "ABD", "AWD") -> {
                    showFixtureResultsUnavailable(cancelled, fixtureStatus)
                    Time.text = fixtureStatus
                }

                fixtureStatus == "NS" -> {
                    Time.text = formattedTime
                    hideGoals(HomeGoals, AwayGoals)
                    cancelled.visibility = View.GONE
                }

                else -> {
                    // Fallback for any unhandled statuses
                    Time.text = formattedTime
                    hideGoals(HomeGoals, AwayGoals)
                    cancelled.visibility = View.GONE
                    Log.d("FavoritesAdapter", "Unhandled fixtureStatus: '$fixtureStatus'")
                }
            }

            root.setOnClickListener {
                onItemClick?.invoke(item)
            }
        }
    }

    override fun getItemCount(): Int = favorites.size

    @SuppressLint("NotifyDataSetChanged")
    fun updateList(newList: List<FavoriteFixtureEntity>) {
        favorites = newList
        notifyDataSetChanged()
    }

    // ---------------- Helper Functions ----------------

    @SuppressLint("NewApi")
    private fun formatIsoTime(isoTime: String): String {
        return try {
            val offsetDateTime = OffsetDateTime.parse(isoTime)
            val zonedDateTime = offsetDateTime.atZoneSameInstant(ZoneId.systemDefault())
            DateTimeFormatter.ofPattern("HH:mm", Locale.getDefault()).format(zonedDateTime)
        } catch (e: Exception) {
            "N/A"
        }
    }

    private fun setGoals(homeView: View, awayView: View, homeGoals: String?, awayGoals: String?) {
        (homeView as? android.widget.TextView)?.text = homeGoals ?: "-"
        (awayView as? android.widget.TextView)?.text = awayGoals ?: "-"
        homeView.visibility = View.VISIBLE
        awayView.visibility = View.VISIBLE
    }

    private fun hideGoals(homeView: View, awayView: View) {
        homeView.visibility = View.GONE
        awayView.visibility = View.GONE
    }

    private fun showFixtureResultsUnavailable(view: View, fixtureStatus: String?) {
        view.visibility = View.VISIBLE
        (view as? android.widget.TextView)?.text = fixtureStatus ?: ""
    }
}
