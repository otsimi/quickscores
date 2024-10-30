package com.live.quickscores.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.live.quickscores.R
import com.live.quickscores.standingsresponse.StandingsResponse
import com.live.quickscores.utils.LOGO_URL
import com.squareup.picasso.Picasso

class LeagueTableAdapter(private val teamList: List<StandingsResponse>):RecyclerView.Adapter<LeagueTableAdapter.LeagueTableViewHolder>() {

class LeagueTableViewHolder(view: View):RecyclerView.ViewHolder(view){
    val teamPosition: TextView = view.findViewById(R.id.teamPosition)
    val teamLogo: ImageView = view.findViewById(R.id.teamLogo)
    val teamName: TextView = view.findViewById(R.id.teamName)
    val teamPoints: TextView = view.findViewById(R.id.teamPoints)
    val teamGD: TextView = view.findViewById(R.id.teamGD)
    val teamGP: TextView = view.findViewById(R.id.teamGP)
    val teamWins: TextView = view.findViewById(R.id.teamWins)
    val teamLosses: TextView = view.findViewById(R.id.teamLosses)
    val teamDraws: TextView = view.findViewById(R.id.teamDraws)
}


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): LeagueTableAdapter.LeagueTableViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.league_table_items, parent, false)
        return LeagueTableViewHolder(view)
    }

    override fun onBindViewHolder(holder: LeagueTableAdapter.LeagueTableViewHolder, position: Int) {
        val team = teamList[position]
        holder.teamPosition.text=team.response[0].league.standings[0].rank.toString()
        holder.teamName.text=team.response[0].league.standings[0].team.name
        if (team.response[0].league.standings[0].team.logo.isNotEmpty()){
            Picasso.get().load("$LOGO_URL${team.response[0].league.standings[0].team.id}.png").into(holder.teamLogo)
        }
        holder.teamPoints.text=team.response[0].league.standings[0].points.toString()
        holder.teamGD.text=team.response[0].league.standings[0].goalsDiff.toString()
        holder.teamGP.text=team.response[0].league.standings[0].all.played.toString()
        holder.teamWins.text=team.response[0].league.standings[0].all.win.toString()
        holder.teamLosses.text=team.response[0].league.standings[0].all.lose.toString()
        holder.teamDraws.text=team.response[0].league.standings[0].all.draw.toString()
    }

    override fun getItemCount(): Int {
        return teamList.size
    }


}