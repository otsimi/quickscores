package com.live.quickscores.adapters

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
    private val fixtureList: List<Response>, private val fixtureClickListener: OnFixtureClickListener,private val leagueClickListener:OnLeagueItemClickListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val TYPE_HEADER = 0
        private const val TYPE_ITEM = 1
        private const val TAG = "RecyclerViewAdapter"
    }
    var onFavoriteClickListener: ((Response) -> Unit)? = null

    interface OnFixtureClickListener {
        fun onFixtureClick(match: Response)
    }
    interface OnLeagueItemClickListener{
        fun onLeagueClick(leagueId: Int, leagueName: String, country: String,leagueLogo:String,season:Int)
    }

    inner class TitleViewHolder(private val binding: CompetitionTitleBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(leagueName: String, country: String, leagueId: Int,leagueLogo:String,season:Int) {
            binding.league.text = leagueName
            binding.country.text = country
            Picasso.get().load("$LEAGUE_LOGO_URL$leagueId.png").into(binding.leagueLogo)
            binding.root.setOnClickListener{
                leagueClickListener.onLeagueClick(leagueId,leagueName,country,leagueLogo,season)
            }
        }

    }

    inner class MatchViewHolder(private val binding: MatchesBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(match: Response) {
            Log.d(TAG, "Binding Match: ${match.teams.home.name} vs ${match.teams.away.name}")
            binding.HomeTeam.text = match.teams.home.name
            binding.AwayTeam.text = match.teams.away.name
            loadImage(match.teams.home.logo, match.teams.home.id, binding.HomeLogo)
            loadImage(match.teams.away.logo, match.teams.away.id, binding.AwayLogo)

            val formattedTime = convertToLocalTime(match.fixture.date)
            val fixtureStatus = match.fixture.status.short
            val matchPeriod = match.fixture.status.elapsed
            println("${fixtureStatus}, ${matchPeriod}, Malenge live match")
            hideGoals(binding)

            binding.favoriteIcon.setColorFilter(
                ContextCompat.getColor(
                    binding.root.context,
                    if (match.isFavorite) R.color.orange_red else R.color.grey
                )
            )

            binding.favoriteIcon.setOnClickListener {
                match.isFavorite = !match.isFavorite

                val colorRes = if (match.isFavorite) R.color.orange_red else R.color.grey
                binding.favoriteIcon.setColorFilter(
                    ContextCompat.getColor(binding.root.context, colorRes)
                )

                onFavoriteClickListener?.invoke(match)
                Log.d(TAG, "Favorite clicked for ${match.teams.home.name}: ${match.isFavorite}")
            }



            when {
                fixtureStatus == "1H" || fixtureStatus == "2H" || fixtureStatus == "ET" -> {
                    binding.Time.text = "${matchPeriod}'"
                    binding.Time.setTextColor(ContextCompat.getColor(binding.root.context, R.color.orange_red))
                    setGoals(binding, match.goals.home, match.goals.away)
                    binding.cancelled.visibility=View.GONE
                }
                fixtureStatus == "INT" -> {
                    binding.Time.text = "INT (${matchPeriod}')"
//                    binding.Time.setTextColor(ContextCompat.getColor(binding.root.context, R.color.orange_red))
                    setGoals(binding, match.goals.home, match.goals.away)
                }
                fixtureStatus=="NS"&& match.fixture.status.short == "CANC"->{
                    hideGoals(binding)
                    binding.Time.text=formattedTime
                }
                fixtureStatus == "NS" && match.fixture.status.short == "PST" -> {
                    binding.Time.text = formattedTime
                }
                ((match.goals.home ) > 0 || (match.goals.away) > 0) && (fixtureStatus=="HT"||fixtureStatus=="FT"||fixtureStatus=="AET"||fixtureStatus=="PEN") -> {
                    binding.Time.text = fixtureStatus
                    setGoals(binding, match.goals.home, match.goals.away)
                    binding.cancelled.visibility=View.GONE
                }
                fixtureStatus == "PST" || fixtureStatus == "CANC" || fixtureStatus == "ABD"||fixtureStatus=="AWD" -> {
                    showFixtureResultsUnavailable(binding, fixtureStatus)
                    binding.Time.text=fixtureStatus

                }

                fixtureStatus == "NS" -> {
                    binding.Time.text = formattedTime
                    hideGoals(binding)
                    binding.cancelled.visibility=View.GONE
                }
                fixtureStatus == "HT" || fixtureStatus == "BT" || fixtureStatus == "P" -> {
                    binding.Time.text = fixtureStatus
//                    binding.Time.setTextColor(ContextCompat.getColor(binding.root.context, R.color.orange_red))
                    setGoals(binding, match.goals.home, match.goals.away)
                    binding.cancelled.visibility=View.GONE
                }
                fixtureStatus == "FT" || fixtureStatus == "AET" || fixtureStatus == "PEN" -> {
                    binding.Time.text = fixtureStatus
                    setGoals(binding, match.goals.home, match.goals.away)
                    binding.cancelled.visibility=View.GONE
                }
                else -> {
                    println("${fixtureStatus},match statuses, Malenge")
                }
            }
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

    @SuppressLint("SetTextI18n")
    private fun setGoals(binding: MatchesBinding, homeGoals: Int?, awayGoals: Int?,) {

        if (homeGoals != null) {
            binding.HomeGoals.visibility = View.VISIBLE
            binding.HomeGoals.text = homeGoals.toString()
        } else {
            binding.HomeGoals.visibility = View.GONE
        }

        if (awayGoals != null) {
            binding.AwayGoals.visibility = View.VISIBLE
            binding.AwayGoals.text = awayGoals.toString()
        } else {
            binding.AwayGoals.visibility = View.GONE
        }
    }
    private fun showFixtureResultsUnavailable(binding: MatchesBinding, status: String) {
        binding.cancelled.visibility = View.VISIBLE
        binding.cancelled.text = when (status) {
            "PST" -> "Postponed"
            "CANC" -> "Cancelled"
            "ABD" -> "Abandoned"
            "AWD"->"Technical Loss"
            else -> "N/A"
        }
        hideGoals(binding)
        binding.Time.text=status
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
            val binding = CompetitionTitleBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            TitleViewHolder(binding)
        } else {
            val binding = MatchesBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
            MatchViewHolder(binding)
        }
    }

    override fun getItemCount(): Int {
        var count = 0
        var lastLeagueId = -1
        fixtureList.forEach { fixture ->
            if (fixture.league.id != lastLeagueId) {
                count++
                lastLeagueId = fixture.league.id
            }
            count++
        }
        Log.d(TAG, "getItemCount: Total items = $count")
        return count
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = getItemAtPosition(position)) {
            is HeaderItem -> if (holder is TitleViewHolder) {
                holder.bind(item.leagueName, item.country, item.leagueId,item.leagueLogo,item.season)
            }
            is MatchItem -> if (holder is MatchViewHolder) {
                holder.bind(item.match)
            }
        }
    }

    private fun isHeader(position: Int): Boolean {
        return getItemAtPosition(position) is HeaderItem
    }

    private sealed class ListItem
    private data class HeaderItem(val leagueName: String, val country: String, val leagueId: Int,val leagueLogo:String,val season:Int) :
        ListItem()

    private data class MatchItem(val match: Response) : ListItem()

    private fun getItemAtPosition(position: Int): ListItem {
        var offset = 0
        var lastLeagueId = -1

        fixtureList.forEach { fixture ->
            if (fixture.league.id != lastLeagueId) {
                if (offset == position) {
                    return HeaderItem(
                        leagueName = fixture.league.name,
                        country = fixture.league.country,
                        leagueId = fixture.league.id,
                        leagueLogo=fixture.league.logo,
                        season = fixture.league.season
                    )
                }
                offset++
                lastLeagueId = fixture.league.id
            }

            if (offset == position) {
                return MatchItem(fixture)
            }
            offset++
        }

        throw IllegalStateException("Invalid position: $position")
    }

    @SuppressLint("NewApi")
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