package com.live.quickscores.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.live.quickscores.R
import com.live.quickscores.databinding.MatchesBinding
import com.live.quickscores.fixtureresponse.Response
import com.squareup.picasso.Picasso
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class LeagueFixturesAdapter(
    private val groupedFixtures: Map<String, List<Response>>,
    private val fixtureClickListener: OnFixtureClickListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    interface OnFixtureClickListener {
        fun onFixtureClick(match: Response)
    }

    companion object {
        private const val TYPE_HEADER = 0
        private const val TYPE_ITEM = 1
    }

    private val itemList = mutableListOf<Pair<String?, Response?>>()

    init {
        for ((date, fixtures) in groupedFixtures) {
            itemList.add(date to null)
            fixtures.forEach { fixture ->
                itemList.add(null to fixture)
            }
        }
    }

    inner class LeagueFixturesViewHolder(val binding: MatchesBinding) : RecyclerView.ViewHolder(binding.root),View.OnClickListener {
        val homeTeam: TextView = binding.HomeTeam
        val awayTeam: TextView = binding.AwayTeam
        val homeGoals: TextView = binding.HomeGoals
        val awayGoals: TextView = binding.AwayGoals
        val homeTeamLogo: ImageView = binding.HomeLogo
        val awayTeamLogo: ImageView = binding.AwayLogo
        val matchTime: TextView = binding.Time

        init {
            binding.root.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            if (position != RecyclerView.NO_POSITION) {
                itemList[position].second?.let { fixtureClickListener.onFixtureClick(it) }
            }
        }

    }

    inner class DateHeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val dateHeader: TextView = itemView.findViewById(R.id.dateHeader)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == TYPE_HEADER) {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.date_header, parent, false)
            DateHeaderViewHolder(view)
        } else {
            val binding = MatchesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            LeagueFixturesViewHolder(binding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = itemList[position]
        when {
            getItemViewType(position) == TYPE_HEADER -> {
                (holder as DateHeaderViewHolder).dateHeader.text = item.first
            }
            else -> {
                val itemHolder = holder as LeagueFixturesViewHolder
                val fixture = item.second

                fixture?.let {
                    // Set team names and logos
                    itemHolder.homeTeam.text = it.teams.home.name
                    itemHolder.awayTeam.text = it.teams.away.name
                    Picasso.get().load(it.teams.home.logo).into(itemHolder.homeTeamLogo)
                    Picasso.get().load(it.teams.away.logo).into(itemHolder.awayTeamLogo)

                    // Handle fixture statuses
                    val matchPeriod = it.fixture.status.elapsed
                    when (val fixtureStatus = it.fixture.status.short) {
                        "NS" -> {
                            itemHolder.matchTime.text = convertToLocalTime(it.fixture.date)
                            itemHolder.matchTime.visibility = View.VISIBLE
                            hideGoals(itemHolder)
                        }
                        "1H", "2H", "HT" -> {
                            itemHolder.matchTime.text = matchPeriod.toString() + "'"
                            itemHolder.matchTime.visibility = View.VISIBLE
                            setGoals(itemHolder, it.goals.home, it.goals.away)
                        }
                        "FT", "AET", "PEN" -> {
                            itemHolder.matchTime.text = fixtureStatus
                            itemHolder.matchTime.visibility = View.VISIBLE
                            setGoals(itemHolder, it.goals.home, it.goals.away)
                        }
                        "PST", "CANC", "ABD", "INT" -> {
                            itemHolder.matchTime.text = fixtureStatus
                            showFixtureResultsUnavailable(itemHolder, fixtureStatus)
                        }
                        else -> {
                            println("Unhandled status: $fixtureStatus")
                        }
                    }
                }
            }
        }
    }
    private fun setGoals(holder: LeagueFixturesViewHolder, homeGoals: Int?, awayGoals: Int?) {
        if (homeGoals != null) {
            holder.homeGoals.visibility = View.VISIBLE
            holder.homeGoals.text = homeGoals.toString()
        } else {
            holder.homeGoals.visibility = View.GONE
        }

        if (awayGoals != null) {
            holder.awayGoals.visibility = View.VISIBLE
            holder.awayGoals.text = awayGoals.toString()
        } else {
            holder.awayGoals.visibility = View.GONE
        }
    }
    private fun showFixtureResultsUnavailable(holder: LeagueFixturesViewHolder, status: String) {
        holder.matchTime.text = when (status) {
            "PST" -> "Postponed"
            "CANC" -> "Cancelled"
            "ABD" -> "Abandoned"
            "AWD" -> "Technical Loss"
            else -> "N/A"
        }
        hideGoals(holder)
    }
    private fun hideGoals(holder: LeagueFixturesViewHolder) {
        holder.homeGoals.visibility = View.GONE
        holder.awayGoals.visibility = View.GONE
    }


    override fun getItemCount(): Int = itemList.size

    override fun getItemViewType(position: Int): Int {
        return if (itemList[position].second == null) TYPE_HEADER else TYPE_ITEM
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
