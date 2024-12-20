package com.live.quickscores.viewmodelclasses

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.live.quickscores.repositories.LeagueTableRepository
import com.live.quickscores.standingsresponse.StandingsResponse
import kotlinx.coroutines.launch
import retrofit2.Response

class LeagueTableViewModel(private val repository: LeagueTableRepository):ViewModel(){
    private val tableStanding=MutableLiveData<Response<StandingsResponse>>()
    val table:LiveData<Response<StandingsResponse>>get() = tableStanding

    fun getTable(leagueId: String, season: String){
        println("getTable called with leagueId: $leagueId")
        viewModelScope.launch {
            try {
                val response=repository.getLeagueTable(leagueId,season)
                if (response != null) {
                    tableStanding.value = response
                    println("${response}, MrimaKenya")
                } else {
                    println("Response is null, MrimaKenya")
                }
            } catch (e:Exception){
                println(e)
            }
        }
    }
}
class LeagueTableViewModelFactory(private val repository: LeagueTableRepository):ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        if (modelClass.isAssignableFrom(LeagueTableViewModel::class.java)){
            return LeagueTableViewModel(repository) as T
        }
        throw IllegalArgumentException("Uknown ViewModel scope")
    }
}