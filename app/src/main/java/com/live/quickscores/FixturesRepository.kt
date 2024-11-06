package com.live.quickscores

import com.live.quickscores.fixturesresponse.FixturesResponse
import retrofit2.Response

class FixturesRepository() {
    private val apiService: ApiService=RetrofitClient().getRetrofitInstance()
    suspend fun fetchFixtures(date: String): Response<FixturesResponse> {
        return apiService.fetchFixtures(date)
    }
}