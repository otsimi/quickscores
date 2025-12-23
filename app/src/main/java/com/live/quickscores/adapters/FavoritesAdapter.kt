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
import com.live.quickscores.fixtureresponse.Response
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

class FavoritesAdapter(
    private var favorites: List<FavoriteFixtureEntity> = emptyList(),
    private val onItemClick: ((FavoriteFixtureEntity, Int?, Int?) -> Unit)? = null
) : RecyclerView.Adapter<FavoritesAdapter.FavoriteViewHolder>() {
    private var liveMap: Map<Int, Response> = emptyMap()

    inner class FavoriteViewHolder(val binding: MatchesBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val binding = MatchesBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )

        binding.favoriteIcon.visibility = View.GONE
        binding.cancelled.visibility = View.GONE

        return FavoriteViewHolder(binding)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        val item = favorites[position]
        val liveFixture = liveMap[item.fixtureId]

        with(holder.binding) {
            HomeTeam.text = item.homeTeam
            AwayTeam.text = item.awayTeam

            Glide.with(root.context)
                .load(item.homeLogo)
                .into(HomeLogo)

            Glide.with(root.context)
                .load(item.awayLogo)
                .into(AwayLogo)

            val fixtureStatus = liveFixture?.fixture?.status?.short
            println("${fixtureStatus},MalengeLiveMatchStatus")
            val matchMinute =
                liveFixture?.fixture?.status?.elapsed?.toString()
            println("${matchMinute},MalengeLiveMatchMinute")

            val homeGoals =
                liveFixture?.goals?.home?.toString()
            println("${liveFixture},MalengeLiveFixture")

            val awayGoals =
                liveFixture?.goals?.away?.toString()

            val formattedKickoff = formatIsoTime(item.time)

            when (fixtureStatus) {
                in listOf("1H", "2H", "ET") -> {
                    Time.text = matchMinute?.let { "$it'" } ?: "LIVE"
                    Time.setTextColor(
                        ContextCompat.getColor(
                            root.context,
                            R.color.orange_red
                        )
                    )
                    setGoals(HomeGoals, AwayGoals, homeGoals, awayGoals)
                    cancelled.visibility = View.GONE
                }

                "HT" -> {
                    Time.text = "HT"
                    Time.setTextColor(
                        ContextCompat.getColor(
                            root.context,
                            R.color.orange_red
                        )
                    )
                    setGoals(HomeGoals, AwayGoals, homeGoals, awayGoals)
                }

                in listOf("FT", "AET", "PEN") -> {
                    Time.text = fixtureStatus
                    setGoals(HomeGoals, AwayGoals, homeGoals, awayGoals)
                    cancelled.visibility = View.GONE
                }

                in listOf("PST", "CANC", "ABD", "AWD") -> {
                    showFixtureResultsUnavailable(cancelled, fixtureStatus)
                    Time.text = fixtureStatus
                    hideGoals(HomeGoals, AwayGoals)
                }

                "NS", null -> {
                    Time.text = formattedKickoff
                    hideGoals(HomeGoals, AwayGoals)
                    cancelled.visibility = View.GONE
                }

                else -> {
                    Log.d(
                        "FavoritesAdapter",
                        "Unhandled fixtureStatus: $fixtureStatus"
                    )
                    Time.text = formattedKickoff
                    hideGoals(HomeGoals, AwayGoals)
                }
            }

            root.setOnClickListener {
                onItemClick?.invoke(item, awayGoals?.toInt(), homeGoals?.toInt())
            }
        }
    }

    override fun getItemCount(): Int = favorites.size


    @SuppressLint("NotifyDataSetChanged")
    fun updateFavorites(newFavorites: List<FavoriteFixtureEntity>) {
        favorites = newFavorites
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateLiveStats(newLiveStats: List<Response>) {
        liveMap = newLiveStats.associateBy { it.fixture.id }
        Log.d("FavoritesAdapter", "Live stats received: ${newLiveStats.size}")

        notifyDataSetChanged()
    }


    @SuppressLint("NewApi")
    private fun formatIsoTime(isoTime: String): String {
        return try {
            val offsetDateTime = OffsetDateTime.parse(isoTime)
            val zonedDateTime =
                offsetDateTime.atZoneSameInstant(ZoneId.systemDefault())
            DateTimeFormatter
                .ofPattern("HH:mm", Locale.getDefault())
                .format(zonedDateTime)
        } catch (e: Exception) {
            "N/A"
        }
    }

    private fun setGoals(
        homeView: View,
        awayView: View,
        homeGoals: String?,
        awayGoals: String?
    ) {
        (homeView as? android.widget.TextView)?.text = homeGoals ?: "-"
        (awayView as? android.widget.TextView)?.text = awayGoals ?: "-"
        homeView.visibility = View.VISIBLE
        awayView.visibility = View.VISIBLE
    }

    private fun hideGoals(homeView: View, awayView: View) {
        homeView.visibility = View.GONE
        awayView.visibility = View.GONE
    }

    private fun showFixtureResultsUnavailable(
        view: View,
        fixtureStatus: String?
    ) {
        view.visibility = View.VISIBLE
        (view as? android.widget.TextView)?.text = fixtureStatus ?: ""
    }
}
