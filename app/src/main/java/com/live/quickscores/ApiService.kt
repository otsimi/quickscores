package com.live.quickscores

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header

interface ApiService {
    @GET("v3/fixtures?")
    fun fetchFixtures(@Header(RAPID_API_KEY) apikey:String):Call<FixturesResponse>
}