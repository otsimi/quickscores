package com.live.quickscores.repositories

import android.util.Log
import com.live.quickscores.RetrofitClient
import com.live.quickscores.allleaguesresponse.League
import com.live.quickscores.services.ApiService
import com.live.quickscores.teamsresponse.Team


class TeamsRepository {
    private val apiService: ApiService = RetrofitClient().getRetrofitInstance()
    private val popularLeagueIds = listOf(
        39,  // Premier League
        140, // La Liga
        135, // Serie A
        78,  // Bundesliga
        61,  // Ligue 1
        2    // Champions League
    )
    private val currentSeason = 2025
    suspend fun getPopularLeagues(): Result<List<League>> = runCatching {
        val response = apiService.getCurrentLeagues()
        if (!response.isSuccessful) {
            error("Leagues API failed: ${response.code()}")
        }
        response.body()?.response
            ?.map { it.league }
            ?.filter { popularLeagueIds.contains(it.id) }
            ?: emptyList()
    }
    suspend fun getTeamsFromPopularLeagues(): Result<List<Team>> = runCatching {

        val allTeams = mutableListOf<Team>()

        for (leagueId in popularLeagueIds) {
            try {
                val response = apiService.getTeamsByLeague(
                    leagueId = leagueId,
                    season = currentSeason
                )

                if (response.isSuccessful) {
                    val teams = response.body()
                        ?.response
                        ?.map { it.team }
                        ?: emptyList()

                    allTeams.addAll(teams)
                } else {
                    Log.w(
                        "TeamsRepository",
                        "Failed teams fetch for league $leagueId"
                    )
                }

            } catch (e: Exception) {
                Log.e(
                    "TeamsRepository",
                    "Error fetching teams for league $leagueId",
                    e
                )
            }
        }

        allTeams.distinctBy { it.id }
    }
}