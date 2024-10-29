package com.live.quickscores

import com.live.quickscores.dataclasses.FixturesResponse
import retrofit2.Response

class UpcomingFixturesRepository {
    private val apiService:ApiService=RetrofitClient().getRetrofitInstance()
    suspend fun getUpcomingHomeTeamFixtures(teamId:String):Response<FixturesResponse>{
        return apiService.fetchFixturesByTeamId(teamId)
    }
    suspend fun getUpcomingAwayTeamFixtures(teamId: String):Response<FixturesResponse>{
        return apiService.fetchFixturesByTeamId(teamId)
    }
}