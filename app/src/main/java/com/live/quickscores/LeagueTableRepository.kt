package com.live.quickscores

import com.live.quickscores.standingsresponse.StandingsResponse
import retrofit2.Response

class LeagueTableRepository {
    private val apiService:ApiService=RetrofitClient().getRetrofitInstance()
    suspend fun getLeagueTable(leagueId:String):Response<StandingsResponse>{
        return apiService.getLeagueStandings(leagueId)
    }
}