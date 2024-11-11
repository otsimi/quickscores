package com.live.quickscores.repositories

import com.live.quickscores.services.ApiService
import com.live.quickscores.RetrofitClient
import com.live.quickscores.fixturesresponse.FixturesResponse
import retrofit2.Response

class FixturesRepository() {
    private val apiService: ApiService = RetrofitClient().getRetrofitInstance()
    suspend fun fetchFixtures(date: String): Response<FixturesResponse> {
        return apiService.fetchFixtures(date)
    }
}