package com.live.quickscores.repositories

import com.live.quickscores.services.ApiService
import com.live.quickscores.RetrofitClient
import com.live.quickscores.fixtureresponse.FixtureResponse
import retrofit2.Response

class StatisticsRepository {
    private val apiService: ApiService = RetrofitClient().getRetrofitInstance()
    suspend fun fetchFixtureStatistics(fixtureId:String):Response<FixtureResponse>{
        return apiService.fetchFixtureStatistics(fixtureId)
    }

}