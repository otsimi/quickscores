package com.live.quickscores

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.live.quickscores.leagueResponse.LeaguesResponse
import kotlinx.coroutines.launch
import retrofit2.Response

class AllLeaguesViewModel(private val repo: AllLeaguesRepo):ViewModel(){
    private val countryLeagues= MutableLiveData<Response<LeaguesResponse>>()
    val leagues: LiveData<Response<LeaguesResponse>> get() = countryLeagues

    fun fetchAllLeagues(){
        viewModelScope.launch {
            try {
                val response=repo.getAllLeagues()
                countryLeagues.value=response
                println("${response},leaguesFetched")
            } catch (e:Exception){
                println(e)
            }
        }
    }
}
class AllLeaguesViewModelFactoryProvider(private val repo: AllLeaguesRepo): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AllLeaguesViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AllLeaguesViewModel(repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}