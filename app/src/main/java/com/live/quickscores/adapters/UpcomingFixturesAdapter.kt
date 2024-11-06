package com.live.quickscores.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.live.quickscores.R
import com.live.quickscores.fixturesresponse.Response

class UpcomingFixturesAdapter(
    private var fixtureList: List<Response>
) : RecyclerView.Adapter<UpcomingFixturesAdapter.FixtureViewHolder>() {

    inner class FixtureViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val homeTeam: TextView = itemView.findViewById(R.id.homeTeamName)
        val fixtureDate: TextView = itemView.findViewById(R.id.fixtureDate)
        val awayTeam: TextView = itemView.findViewById(R.id.awayTeamName)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FixtureViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.upcoming_fixtures, parent, false)
        return FixtureViewHolder(view)
    }

    override fun getItemCount(): Int = fixtureList.size

    override fun onBindViewHolder(holder: FixtureViewHolder, position: Int) {
        val fixture = fixtureList[position]

        holder.homeTeam.text = fixture.teams.home.name
        holder.fixtureDate.text = fixture.fixture.date
        holder.awayTeam.text = fixture.teams.away.name
    }

    fun updateFixtures(newFixtures: List<Response>) {
        fixtureList = newFixtures
        notifyDataSetChanged()
    }
}
