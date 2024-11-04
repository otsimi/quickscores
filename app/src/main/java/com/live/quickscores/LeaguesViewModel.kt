package com.live.quickscores

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.live.quickscores.leagueResponse.LeaguesResponse
import kotlinx.coroutines.launch
import retrofit2.Response

class LeaguesViewModel(private val repository: LeaguesRepository) :ViewModel() {
    private val countryLeagues=MutableLiveData<Response<LeaguesResponse>>()
    val leagues:LiveData<Response<LeaguesResponse>>get() = countryLeagues

    fun fetchLeagues(countryCode:String){
        viewModelScope.launch {
            try {
                val response=repository.getLeagues(countryCode)
                countryLeagues.value=response
            }catch (e:Exception){
                println(e)
            }

        }
    }
}
class LeaguesViewModelFactoryProvider(private val repository: LeaguesRepository):ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LeaguesViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LeaguesViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}