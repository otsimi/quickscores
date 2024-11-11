package com.live.quickscores.repositories

import com.live.quickscores.services.ApiService
import com.live.quickscores.RetrofitClient
import com.live.quickscores.fixturesresponse.FixturesResponse
import retrofit2.Response

class LeagueFixturesRepo {
    private val apiService: ApiService = RetrofitClient().getRetrofitInstance()
    suspend fun getLeagueFixturesById(leagueId:String,season: String,fromDate:String,toDate:String):Response<FixturesResponse>{
        return apiService.getFixturesByLeagueId(leagueId,season,fromDate,toDate)

    }
}