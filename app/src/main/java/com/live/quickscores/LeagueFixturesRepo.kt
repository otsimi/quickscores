package com.live.quickscores

import com.live.quickscores.fixturesresponse.FixturesResponse
import retrofit2.Response

class LeagueFixturesRepo {
    private val apiService:ApiService=RetrofitClient().getRetrofitInstance()
    suspend fun getLeagueFixturesById(leagueId:String,season: String,fromDate:String,toDate:String):Response<FixturesResponse>{
        return apiService.getFixturesByLeagueId(leagueId,season,fromDate,toDate)

    }
}