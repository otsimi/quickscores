package com.live.quickscores

import com.live.quickscores.dataclasses.FixturesResponse
import com.live.quickscores.leagueResponse.Season
import retrofit2.Response

class LeagueFixturesRepo {
    private val apiService:ApiService=RetrofitClient().getRetrofitInstance()
    suspend fun getLeagueFixturesById(date:String,leagueId:String,season: String):Response<FixturesResponse>{
        return apiService.getFixturesByLeagueId(date,leagueId,season)

    }
}