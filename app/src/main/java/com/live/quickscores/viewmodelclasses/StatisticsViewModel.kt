package com.live.quickscores.viewmodelclasses

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.live.quickscores.fixtureresponse.FixtureResponse
import com.live.quickscores.repositories.StatisticsRepository
import kotlinx.coroutines.launch
import retrofit2.Response

class StatisticsViewModel(private val repository: StatisticsRepository):ViewModel() {
    private val fixtureStats=MutableLiveData<Response<FixtureResponse>>()
    val stats:LiveData<Response<FixtureResponse>> get() = fixtureStats

    fun fetchStats(fixtureId:String){
        viewModelScope.launch {
            try {
                val response=repository.fetchFixtureStatistics(fixtureId)
                fixtureStats.value=response
                println("${fixtureStats},fetchedStats:Malenge")
            } catch (e:Exception){
                println(e)
            }
        }
    }
}
class StatisticsViewModelFactory(private val repository: StatisticsRepository):ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        if (modelClass.isAssignableFrom(StatisticsViewModel::class.java)){
            return StatisticsViewModel(repository) as T
        }
        throw IllegalArgumentException("Uknown ViewModel scope")
    }
}