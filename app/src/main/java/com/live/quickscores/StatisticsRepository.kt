package com.live.quickscores

import com.live.quickscores.fixturesresponse.statisticsResponse.StatisticsResponse
import retrofit2.Response

class StatisticsRepository {
    private val apiService: ApiService=RetrofitClient().getRetrofitInstance()
    suspend fun fetchFixtureStatistics(fixtureId:String):Response<StatisticsResponse>{
        return apiService.fetchFixtureStatistics(fixtureId)
    }

}