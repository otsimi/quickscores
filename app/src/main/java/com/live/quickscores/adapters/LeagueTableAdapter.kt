package com.live.quickscores.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.live.quickscores.R
import com.live.quickscores.standingsresponse.Standing
import com.live.quickscores.standingsresponse.StandingsResponse
import com.live.quickscores.utils.LOGO_URL
import com.squareup.picasso.Picasso

class LeagueTableAdapter(private val teamList: List<Standing>) : RecyclerView.Adapter<LeagueTableAdapter.LeagueTableViewHolder>() {

    class LeagueTableViewHolder(view: View) : RecyclerView.ViewHolder(view) {
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LeagueTableViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.league_table_items, parent, false)
        return LeagueTableViewHolder(view)
    }

    override fun onBindViewHolder(holder: LeagueTableViewHolder, position: Int) {
        val team = teamList[position]
        holder.teamPosition.text = team.rank.toString()
        holder.teamName.text = team.team.name
        Picasso.get().load("$LOGO_URL${team.team.id}.png").into(holder.teamLogo)
        holder.teamPoints.text = team.points.toString()
        holder.teamGD.text = team.goalsDiff.toString()
        holder.teamGP.text = team.all.played.toString()
        holder.teamWins.text = team.all.win.toString()
        holder.teamLosses.text = team.all.lose.toString()
        holder.teamDraws.text = team.all.draw.toString()
    }

    override fun getItemCount(): Int {
        return teamList.size
    }
}
