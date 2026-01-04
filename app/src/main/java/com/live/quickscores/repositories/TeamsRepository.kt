package com.live.quickscores.repositories

import com.live.quickscores.RetrofitClient
import com.live.quickscores.allleaguesresponse.AllLeaguesReponse
import com.live.quickscores.services.ApiService

class TeamsRepository {
    private val apiService: ApiService= RetrofitClient().getRetrofitInstance()
    private val popularLeagueIds=listOf(
        39,//pl
        140,//la liga
        135,// serie A
        78,//bundesliga
        61,//Ligue 1
        2, //Champions league
    )
    private val currentSeason = 2025
//    suspend fun getPopularLeagues():Result<List<AllLeaguesReponse>> = runCatching {
//        val response = apiService.fetchLeagues(type, current)
//    }
}