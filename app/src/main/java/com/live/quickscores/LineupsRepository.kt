package com.live.quickscores

import com.live.quickscores.lineupresponse.LineupsResponse
import retrofit2.Response

class LineupsRepository {
    private val apiService:ApiService=RetrofitClient().getRetrofitInstance()
    suspend fun getMatchLineups(fixtureId:String):Response<LineupsResponse>{
        return apiService.fetchLineups(fixtureId)
    }
}