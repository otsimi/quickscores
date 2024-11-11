package com.live.quickscores.repositories

import com.live.quickscores.services.ApiService
import com.live.quickscores.RetrofitClient
import com.live.quickscores.allleaguesresponse.AllLeaguesReponse
import retrofit2.Response

class AllLeaguesRepo {
    private val apiService: ApiService = RetrofitClient().getRetrofitInstance()
    suspend fun getAllLeagues(): Response<AllLeaguesReponse> {
        val response=apiService.getAllLeagues()
        println("getting response${response.body()}")
        return apiService.getAllLeagues()

    }
}