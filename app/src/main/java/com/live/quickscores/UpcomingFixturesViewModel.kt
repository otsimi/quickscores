package com.live.quickscores

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.live.quickscores.fixturesresponse.FixturesResponse
import kotlinx.coroutines.launch
import retrofit2.Response

class UpcomingFixturesViewModel(private val repository: UpcomingFixturesRepository):ViewModel() {
    private val upcomingHomeTeamFixtures=MutableLiveData<Response<FixturesResponse>>()
    val upcomingHomeFixtures:LiveData<Response<FixturesResponse>>get()=upcomingHomeTeamFixtures

    private val upcomingAwayTeamFixtures=MutableLiveData<Response<FixturesResponse>>()
    val upcomingAwayFixtures:LiveData<Response<FixturesResponse>>get() = upcomingAwayTeamFixtures

    fun getUpcomingHomeFixtures(teamId:String){
        viewModelScope.launch {
            try {
                val response=repository.getUpcomingHomeTeamFixtures(teamId)
                upcomingHomeTeamFixtures.value=response
            } catch (e:Exception){
                println(e)
            }
        }
    }
    fun getUpcomingAwayFixtures(teamId: String){
        viewModelScope.launch {
            try {
                val response=repository.getUpcomingAwayTeamFixtures(teamId)
                upcomingAwayTeamFixtures.value=response
            } catch (e:Exception){
                println(e)
            }
        }
    }
}
class UpcomingFixturesViewModelFactory(private val repository: UpcomingFixturesRepository):ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UpcomingFixturesViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return UpcomingFixturesViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}