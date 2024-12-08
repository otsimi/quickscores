package com.live.quickscores.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
    private val headerList: List<Response>, private val fixtureClickListener: OnFixtureClickListener):RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val TYPE_HEADER = 0
        private const val TYPE_ITEM = 1
    }

    interface OnFixtureClickListener {
        fun onFixtureClick(match: Response)
    }

    inner class TitleViewHolder(private val binding: CompetitionTitleBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(leagueName: String, country: String, leagueId: Int) {
            binding.league.text = leagueName
            binding.country.text = country

            Picasso.get().load("$LEAGUE_LOGO_URL$leagueId.png").into(binding.leagueLogo)
        }
    }

    inner class MatchViewHolder(private val binding: MatchesBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(match: Response) {
            binding.HomeTeam.text = match.teams.home.name
            binding.AwayTeam.text = match.teams.away.name
            val isoDate = match.fixture.date
            val formattedTime = convertToLocalTime(isoDate)
            println("${formattedTime},Murima Imeguswa")

            binding.Time.text = formattedTime
            if (match.teams.home.logo.isNotEmpty()) {
                Picasso.get().load("$LOGO_URL${match.teams.home.id}.png").into(binding.HomeLogo)
            }else{
                binding.HomeLogo.setImageResource(R.drawable.imageholder)
            }
            if (match.teams.away.logo.isNotEmpty()) {
                Picasso.get().load("$LOGO_URL${match.teams.away.id}.png").into(binding.AwayLogo)
            }else{
                binding.AwayLogo.setImageResource(R.drawable.imageholder)
            }
            val homeTeamGoals = match.goals.home
            println("${match.goals.home},Malenge")

            if (homeTeamGoals!= null){
                binding.HomeGoals.visibility = View.VISIBLE
                binding.HomeGoals.text= match.goals.home.toString()

            } else{
                binding.HomeGoals.visibility = View.GONE
            }
            val awayTeamGoals=match.goals.away
            if (awayTeamGoals!=null){
                binding.AwayGoals.visibility=View.VISIBLE
                binding.AwayGoals.text=match.goals.away.toString()
            }
            else{
                binding.AwayGoals.visibility = View.GONE
            }

            binding.root.setOnClickListener {
                fixtureClickListener.onFixtureClick(match)
            }
        }
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

        headerList.forEach { response ->

                if (response.league.id != lastLeagueId) {
                    count++
                    lastLeagueId = response.league.id
                }
                count++

        }
        return count
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItemAtPosition(position)
        when {
            holder is TitleViewHolder && item is HeaderItem -> {
                holder.bind(item.leagueName, item.country, item.leagueId)
            }
            holder is MatchViewHolder && item is MatchItem -> {
                holder.bind(item.match)
            }
        }
    }

    private fun isHeader(position: Int): Boolean {
        return getItemAtPosition(position) is HeaderItem
    }

    private sealed class ListItem
    private data class HeaderItem(val leagueName: String, val country: String, val leagueId: Int) : ListItem()
    private data class MatchItem(val match: Response) : ListItem()

    private fun getItemAtPosition(position: Int): ListItem {
        var offset = 0
        var lastLeagueId = -1

        headerList.forEach { response ->
                if (response.league.id != lastLeagueId) {
                    if (offset == position) {
                        return HeaderItem(
                            leagueName = response.league.name,
                            country =response.league.country,
                            leagueId = response.league.id
                        )
                    }
                    offset++
                    lastLeagueId = response.league.id
                }

                if (offset == position) {
                    return MatchItem(response)
                }
                offset++

        }
        throw IllegalStateException("Invalid position")
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
