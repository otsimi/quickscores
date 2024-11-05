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
import com.live.quickscores.dataclasses.FixturesResponse
import com.squareup.picasso.Picasso
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

class LeagueFixturesAdapter(private val fixtureLists: List<FixturesResponse>, private val fixtureClickListener: OnFixtureClickListener):RecyclerView.Adapter<LeagueFixturesAdapter.LeagueFixturesViewHolder>() {

    interface OnFixtureClickListener {
        fun onFixtureClick(match: FixturesResponse)
    }

    inner class LeagueFixturesViewHolder(val binding:MatchesBinding):RecyclerView.ViewHolder(binding.root){
        val homeTeam:TextView=binding.HomeTeam
        val awayTeam:TextView=binding.AwayTeam
        val homeGoals:TextView=binding.HomeGoals
        val awayGoals:TextView=binding.AwayGoals
        val homeTeamLogo:ImageView=binding.HomeLogo
        val awayTeamLogo:ImageView=binding.AwayLogo
        val matchTime:TextView=binding.Time

        init {
            binding.root.setOnClickListener{
                val position = adapterPosition
                if (position!=RecyclerView.NO_POSITION){
                    fixtureClickListener.onFixtureClick(fixtureLists[position])
                }
            }
        }
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): LeagueFixturesAdapter.LeagueFixturesViewHolder {
       val binding=MatchesBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return LeagueFixturesViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: LeagueFixturesAdapter.LeagueFixturesViewHolder,
        position: Int
    ) {
        val leagueFixtures=fixtureLists[position]
        holder.homeTeam.text= leagueFixtures.response[0].teams.home.name
        holder.awayTeam.text=leagueFixtures.response[0].teams.away.name
        if (leagueFixtures.response[0].teams.home.logo.isNotEmpty()){
            Picasso.get().load(leagueFixtures.response[0].teams.home.logo).into(holder.homeTeamLogo)
        } else{
            holder.homeTeamLogo.setImageResource(R.drawable.imageholder)
        }
        if (leagueFixtures.response[0].teams.away.logo.isNotEmpty()){
            Picasso.get().load(leagueFixtures.response[0].teams.away.logo).into(holder.awayTeamLogo)
        } else{
            holder.awayTeamLogo.setImageResource(R.drawable.imageholder)
        }
        if (leagueFixtures.response[0].goals.home!=null){
            holder.homeGoals.visibility= View.VISIBLE
            holder.homeGoals.text=leagueFixtures.response[0].goals.home
        } else{
            println()
        }
        if (leagueFixtures.response[0].goals.away!=null){
            holder.awayGoals.visibility=View.VISIBLE
            holder.awayGoals.text=leagueFixtures.response[0].goals.away
        }else{
            println()
        }
        val isoDate = leagueFixtures.response[0].fixture.date
        val formattedTime = convertToLocalTime(isoDate)
        holder.matchTime.text=formattedTime
    }

    override fun getItemCount(): Int {
       return fixtureLists.size
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