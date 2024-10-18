package com.live.quickscores

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface ApiService {
    @GET("v3/fixtures?")
    fun fetchFixtures(
        @Header("x-rapidapi-key") apiKey: String, @Query("date") date: String): Call<FixturesResponse>

}
