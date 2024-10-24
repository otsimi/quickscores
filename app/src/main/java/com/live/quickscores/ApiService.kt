package com.live.quickscores

import com.live.quickscores.dataclasses.FixturesResponse
import com.live.quickscores.dataclasses.StatisticsResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface ApiService {
    @GET("v3/fixtures?")
    fun fetchFixtures(
        @Header("x-rapidapi-key") apiKey: String, @Query("date") date: String): Call<FixturesResponse>

    @GET("v3/fixtures/statistics?")
    fun fetchFixtureStatistics(
        @Header("x-rapidapi-key")apiKey: String,@Query("fixture") fixtureId: String
    ):Call<StatisticsResponse>

}
