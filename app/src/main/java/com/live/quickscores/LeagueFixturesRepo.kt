package com.live.quickscores

import com.live.quickscores.dataclasses.FixturesResponse
import retrofit2.Response

class LeagueFixturesRepo {
    private val apiService:ApiService=RetrofitClient().getRetrofitInstance()
    suspend fun getLeagueFixturesById(date:String,leagueId:String):Response<FixturesResponse>{
        return apiService.getFixturesByLeagueId(date,leagueId)

    }
}