package com.live.quickscores.viewmodelclasses

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.live.quickscores.fixturesresponse.FixturesResponse
import com.live.quickscores.repositories.LeagueFixturesRepo
import kotlinx.coroutines.launch
import retrofit2.Response

class LeagueFixturesViewModel(private val repo: LeagueFixturesRepo):ViewModel() {
    private val leagueFixtures=MutableLiveData<Response<FixturesResponse>>()
    val fixtures:LiveData<Response<FixturesResponse>>get() = leagueFixtures

    fun fetchFixturesByLeagueId(leagueId:String,season:String,fromDate:String,toDate:String){
        viewModelScope.launch {
            try {
                val response=repo.getLeagueFixturesById(leagueId,season,fromDate,toDate)
                leagueFixtures.value=response
            }catch (e:Exception){
                println(e)
            }
        }
    }
}
class LeagueFixturesViewModelFactoryProvider(private val repo: LeagueFixturesRepo):ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LeagueFixturesViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LeagueFixturesViewModel(repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}