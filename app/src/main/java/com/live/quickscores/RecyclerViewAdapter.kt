package com.live.quickscores

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.live.quickscores.databinding.CompetitionTitleBinding
import com.live.quickscores.databinding.MatchesBinding
import com.live.quickscores.dataclasses.FixturesResponse
import com.squareup.picasso.Picasso

        class RecyclerViewAdapter(private val headerList: List<FixturesResponse>, private val fixtureClickListener: OnFixtureClickListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val TYPE_HEADER = 0
        private const val TYPE_ITEM = 1
    }
    interface OnFixtureClickListener {
        fun onFixtureClick(match: Response)
    }

    inner class TitleViewHolder(private val binding: CompetitionTitleBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(competition: FixturesResponse) {
            val firstResponse = competition.response.firstOrNull()
            if (firstResponse != null) {
                binding.league.text = firstResponse.league.name
                if (firstResponse.league.logo.isNotEmpty()){
                    Picasso.get().load(LEAGUE_LOGO_URL+"${firstResponse.league.id}.png").into(binding.leagueLogo)
                    println("${LEAGUE_LOGO_URL+firstResponse.league.id}.png,Gashagua")
                }
                binding.country.text = firstResponse.league.country


            }
        }
    }

    inner class MatchViewHolder(private val binding: MatchesBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(match: Response) {
            binding.HomeTeam.text = match.teams.home.name
            binding.AwayTeam.text = match.teams.away.name
            binding.Time.text = match.fixture.timestamp.toString()
            if (match.teams.home.logo.isNotEmpty()) {
                Picasso.get().load(LOGO_URL + "${match.teams.home.id}.png").into(binding.HomeLogo)
                println("${LOGO_URL + match.teams.home.id},Gachagua")
            }
            if (match.teams.away.logo.isNotEmpty()) {
                Picasso.get().load(LOGO_URL + "${match.teams.away.id}.png").into(binding.AwayLogo)
                println("${LOGO_URL + match.teams.away.id},Gachagua")
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
        return headerList.sumOf { it.response.size + 1 }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is TitleViewHolder) {
            val competition = getCompetitionForPosition(position)
            holder.bind(competition)
        } else if (holder is MatchViewHolder) {
            val match = getMatchForPosition(position)
            holder.bind(match)
        }
    }

    private fun isHeader(position: Int): Boolean {
        var offset = 0
        headerList.forEach { competition ->
            if (position == offset) return true
            offset += competition.response.size + 1
        }
        return false
    }

    private fun getCompetitionForPosition(position: Int): FixturesResponse {
        var offset = 0
        headerList.forEach { competition ->
            if (position == offset) return competition
            offset += competition.response.size + 1
        }
        throw IllegalStateException("Mulima")
    }

    private fun getMatchForPosition(position: Int): Response {
        var offset = 0
        headerList.forEach { competition ->
            offset++
            if (position < offset + competition.response.size) {
                return competition.response[position - offset]
            }
            offset += competition.response.size
        }
        throw IllegalStateException("Mulima")
    }
}



