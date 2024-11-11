package com.live.quickscores.repositories

import com.live.quickscores.services.ApiService
import com.live.quickscores.RetrofitClient
import com.live.quickscores.countriesresponse.CountriesResponse
import retrofit2.Response

class CountriesRepository {
    private val apiService: ApiService = RetrofitClient().getRetrofitInstance()
    suspend fun fetchCountries():Response<CountriesResponse>{
        return apiService.getCountries()
    }
}