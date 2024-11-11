package com.live.quickscores.repositories

import com.live.quickscores.services.ApiService
import com.live.quickscores.RetrofitClient
import com.live.quickscores.fixturesresponse.FixturesResponse
import retrofit2.Response

class UpcomingFixturesRepository {
    private val apiService: ApiService = RetrofitClient().getRetrofitInstance()
    suspend fun getUpcomingHomeTeamFixtures(teamId:String):Response<FixturesResponse>{
        return apiService.fetchFixturesByTeamId(teamId)
    }
    suspend fun getUpcomingAwayTeamFixtures(teamId: String):Response<FixturesResponse>{
        return apiService.fetchFixturesByTeamId(teamId)
    }
}