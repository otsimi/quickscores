package com.live.quickscores.repositories

import com.live.quickscores.services.ApiService
import com.live.quickscores.RetrofitClient
import com.live.quickscores.fixturedataresponse.FixtureDataResponse
import com.live.quickscores.fixtureresponse.FixtureResponse
import retrofit2.Response

class FixturesRepository() {
    private val apiService: ApiService = RetrofitClient().getRetrofitInstance()
    suspend fun fetchFixtures(date: String): Response<FixtureResponse> {
        return apiService.fetchFixtures(date)
    }
}