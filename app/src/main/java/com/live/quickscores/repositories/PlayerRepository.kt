package com.live.quickscores.repositories

import com.live.quickscores.RetrofitClient
import com.live.quickscores.playersresponse.Player
import com.live.quickscores.services.ApiService

class PlayerRepository {
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
    suspend fun getPopularPlayers(): Result<List<Player>> = runCatching {

        val allPlayers = mutableSetOf<Player>()

        for (leagueId in popularLeagueIds) {
            try {
                apiService.getTopScorers(leagueId, currentSeason)
                    .body()?.response
                    ?.forEach { allPlayers.add(it.player) }

                apiService.getTopAssister(leagueId, currentSeason)
                    .body()?.response
                    ?.forEach { allPlayers.add(it.player) }

                apiService.getTopYellowCarded(leagueId, currentSeason)
                    .body()?.response
                    ?.forEach { allPlayers.add(it.player) }

            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        allPlayers.toList()
    }
}