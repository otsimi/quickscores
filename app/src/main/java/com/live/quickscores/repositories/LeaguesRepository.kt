package com.live.quickscores.repositories

import com.live.quickscores.services.ApiService
import com.live.quickscores.RetrofitClient
import com.live.quickscores.leagueResponse.LeaguesResponse
import retrofit2.Response


class LeaguesRepository {
    private val apiService: ApiService = RetrofitClient().getRetrofitInstance()
    suspend fun getLeagues(countryCode:String):Response<LeaguesResponse>{
        return apiService.getLeagues(countryCode)
    }
}