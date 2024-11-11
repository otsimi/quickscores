package com.live.quickscores

import com.live.quickscores.leagueResponse.LeaguesResponse
import retrofit2.Response

class AllLeaguesRepo {
    private val apiService:ApiService = RetrofitClient().getRetrofitInstance()
    suspend fun getAllLeagues(): Response<LeaguesResponse> {
        val response=apiService.getAllLeagues()
        println("getting response${response.body()}")
        return apiService.getAllLeagues()

    }
}