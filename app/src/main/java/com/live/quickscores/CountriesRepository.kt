package com.live.quickscores

import com.live.quickscores.countriesresponse.CountriesResponse
import retrofit2.Response

class CountriesRepository {
    private val apiService:ApiService=RetrofitClient().getRetrofitInstance()
    suspend fun fetchCountries():Response<CountriesResponse>{
        return apiService.getCountries()
    }
}