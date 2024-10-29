package com.live.quickscores

import com.live.quickscores.dataclasses.FixturesResponse
import com.live.quickscores.dataclasses.statisticsResponse.StatisticsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("v3/fixtures?")
    suspend fun fetchFixtures(@Query("date") date: String): Response<FixturesResponse>

   @GET("v3/fixtures/statistics")
    suspend fun fetchFixtureStatistics(@Query("fixture") fixtureId: String): Response<StatisticsResponse>

    @GET("v3/fixtures")
    suspend fun fetchFixturesByTeamId(@Query("id")teamId:String):Response<FixturesResponse>
}
