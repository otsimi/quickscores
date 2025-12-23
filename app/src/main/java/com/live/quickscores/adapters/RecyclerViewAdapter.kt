package com.live.quickscores.adapters

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.live.quickscores.R
import com.live.quickscores.databinding.CompetitionTitleBinding
import com.live.quickscores.databinding.MatchesBinding
import com.live.quickscores.fixtureresponse.Response
import com.live.quickscores.utils.LEAGUE_LOGO_URL
import com.live.quickscores.utils.LOGO_URL
import com.squareup.picasso.Picasso
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

class RecyclerViewAdapter(
    private val fixtureList: List<Response>,
    private val fixtureClickListener: OnFixtureClickListener,
    private val leagueClickListener: OnLeagueItemClickListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var favoriteIds: Set<Int> = emptySet()

    @SuppressLint("NotifyDataSetChanged")
    fun updateFavoriteIds(ids: Set<Int>) {
        Log.d("FavoritesDebug", "Adapter received favorite IDs: $ids")
        favoriteIds = ids.toMutableSet()
        notifyDataSetChanged()
    }



    var onFavoriteClickListener: ((Response) -> Unit)? = null

    companion object {
        private const val TYPE_HEADER = 0
        private const val TYPE_ITEM = 1
        private const val TAG = "RecyclerViewAdapter"
    }

    interface OnFixtureClickListener {
        fun onFixtureClick(match: Response)
    }

    interface OnLeagueItemClickListener {
        fun onLeagueClick(leagueId: Int, leagueName: String, country: String, leagueLogo: String, season: Int)
    }

//    @SuppressLint("NotifyDataSetChanged")
//    fun setFavoriteIds(ids: Set<Int>) {
//        favoriteIds = ids
//    }

    inner class TitleViewHolder(private val binding: CompetitionTitleBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(leagueName: String, country: String, leagueId: Int, leagueLogo: String, season: Int) {
            binding.league.text = leagueName
            binding.country.text = country
            Picasso.get().load("$LEAGUE_LOGO_URL$leagueId.png").into(binding.leagueLogo)
            binding.root.setOnClickListener {
                leagueClickListener.onLeagueClick(leagueId, leagueName, country, leagueLogo, season)
            }
        }
    }

    inner class MatchViewHolder(private val binding: MatchesBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @RequiresApi(Build.VERSION_CODES.O)
        fun bind(match: Response) {
            val fixtureId = match.fixture.id

            binding.HomeTeam.text = match.teams.home.name
            binding.AwayTeam.text = match.teams.away.name
            loadImage(match.teams.home.logo, match.teams.home.id, binding.HomeLogo)
            loadImage(match.teams.away.logo, match.teams.away.id, binding.AwayLogo)


            val formattedTime = convertToLocalTime(match.fixture.date)
            val fixtureStatus = match.fixture.status.short
            val matchPeriod = match.fixture.status.elapsed

            binding.Time.text = when {
                fixtureStatus in listOf("1H", "2H", "ET") -> "${matchPeriod}'"
                fixtureStatus == "INT" -> "INT (${matchPeriod}')"
                fixtureStatus in listOf("NS", "PST", "CANC", "ABD", "AWD") -> formattedTime
                fixtureStatus in listOf("HT", "BT", "P", "FT", "AET", "PEN") -> fixtureStatus
                else -> formattedTime
            }

            // Goals visibility
            if (fixtureStatus in listOf("1H", "2H", "ET", "HT", "BT", "P", "FT", "AET", "PEN", "INT")) {
                setGoals(binding, match.goals.home, match.goals.away)
            } else {
                hideGoals(binding)
            }

            // Favorite icon: use saved favoriteIds
            binding.favoriteIcon.setImageResource(
                if (favoriteIds.contains(fixtureId))
                    R.drawable.favorite_svgrepo_com
                else
                    R.drawable.baseline_star_border_24
            )
            binding.favoriteIcon.setColorFilter(
                ContextCompat.getColor(
                    binding.root.context,
                    if (favoriteIds.contains(fixtureId)) R.color.orange_red else R.color.grey
                )
            )

            binding.favoriteIcon.setOnClickListener {
                onFavoriteClickListener?.invoke(match)
            }

            // Click on match root
            binding.root.setOnClickListener {
                fixtureClickListener.onFixtureClick(match)
            }
        }
    }

    private fun loadImage(logoUrl: String, teamId: Int, imageView: android.widget.ImageView) {
        if (logoUrl.isNotEmpty()) {
            Picasso.get().load("$LOGO_URL$teamId.png").into(imageView)
        } else {
            imageView.setImageResource(R.drawable.imageholder)
        }
    }

    private fun setGoals(binding: MatchesBinding, homeGoals: Int?, awayGoals: Int?) {
        binding.HomeGoals.visibility = if (homeGoals != null) View.VISIBLE else View.GONE
        binding.HomeGoals.text = homeGoals?.toString() ?: ""
        binding.AwayGoals.visibility = if (awayGoals != null) View.VISIBLE else View.GONE
        binding.AwayGoals.text = awayGoals?.toString() ?: ""
    }

    private fun hideGoals(binding: MatchesBinding) {
        binding.HomeGoals.visibility = View.GONE
        binding.AwayGoals.visibility = View.GONE
    }

    override fun getItemViewType(position: Int): Int {
        return if (isHeader(position)) TYPE_HEADER else TYPE_ITEM
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == TYPE_HEADER) {
            val binding = CompetitionTitleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            TitleViewHolder(binding)
        } else {
            val binding = MatchesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            MatchViewHolder(binding)
        }
    }

    override fun getItemCount(): Int {
        var count = 0
        var lastLeagueId = -1
        fixtureList.forEach { fixture ->
            if (fixture.league.id != lastLeagueId) {
                count++ // header
                lastLeagueId = fixture.league.id
            }
            count++ // match item
        }
        return count
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = getItemAtPosition(position)) {
            is HeaderItem -> if (holder is TitleViewHolder) {
                holder.bind(item.leagueName, item.country, item.leagueId, item.leagueLogo, item.season)
            }
            is MatchItem -> if (holder is MatchViewHolder) {
                holder.bind(item.match)
            }
        }
    }

    private fun isHeader(position: Int): Boolean = getItemAtPosition(position) is HeaderItem

    private sealed class ListItem
    private data class HeaderItem(val leagueName: String, val country: String, val leagueId: Int, val leagueLogo: String, val season: Int) : ListItem()
    private data class MatchItem(val match: Response) : ListItem()

    private fun getItemAtPosition(position: Int): ListItem {
        var offset = 0
        var lastLeagueId = -1
        fixtureList.forEach { fixture ->
            if (fixture.league.id != lastLeagueId) {
                if (offset == position) return HeaderItem(fixture.league.name, fixture.league.country, fixture.league.id, fixture.league.logo, fixture.league.season)
                offset++
                lastLeagueId = fixture.league.id
            }
            if (offset == position) return MatchItem(fixture)
            offset++
        }
        throw IllegalStateException("Invalid position: $position")
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun convertToLocalTime(isoDate: String): String {
        return try {
            val offsetDateTime = OffsetDateTime.parse(isoDate)
            val zonedDateTime = offsetDateTime.atZoneSameInstant(java.time.ZoneId.systemDefault())
            DateTimeFormatter.ofPattern("HH:mm", Locale.getDefault()).format(zonedDateTime)
        } catch (e: Exception) {
            "N/A"
        }
    }
}
