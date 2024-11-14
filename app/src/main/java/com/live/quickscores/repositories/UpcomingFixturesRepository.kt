package com.live.quickscores.repositories

import com.live.quickscores.services.ApiService
import com.live.quickscores.RetrofitClient
import com.live.quickscores.fixtureresponse.FixtureResponse
import retrofit2.Response

class UpcomingFixturesRepository {
    private val apiService: ApiService = RetrofitClient().getRetrofitInstance()
    suspend fun getUpcomingHomeTeamFixtures(teamId:String):Response<FixtureResponse>{
        return apiService.fetchFixturesByTeamId(teamId)
    }
    suspend fun getUpcomingAwayTeamFixtures(teamId: String):Response<FixtureResponse>{
        return apiService.fetchFixturesByTeamId(teamId)
    }
}