package com.live.quickscores

import com.live.quickscores.standingsresponse.StandingsResponse
import retrofit2.Response

class LeagueTableRepository {
    private val apiService:ApiService=RetrofitClient().getRetrofitInstance()
    suspend fun getLeagueTable(leagueId: String, season: String):Response<StandingsResponse>{
        println("Calling getLeagueStandings with leagueId: $leagueId")
        val response = apiService.getLeagueStandings(leagueId, season)
        if (response.isSuccessful) {
            println("API call successful, response body: ${response.body()}")
        } else {
            println("API call failed, error code: ${response.code()}, error body: ${response.errorBody()?.string()}")
        }
        return response
    }
}