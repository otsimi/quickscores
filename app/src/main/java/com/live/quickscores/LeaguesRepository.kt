package com.live.quickscores

import com.live.quickscores.leagueResponse.LeaguesResponse
import com.live.quickscores.leagueResponse.Season
import retrofit2.Response


class LeaguesRepository {
    private val apiService:ApiService = RetrofitClient().getRetrofitInstance()
    suspend fun getLeagues(countryCode:String):Response<LeaguesResponse>{
        return apiService.getLeagues(countryCode)
    }
}