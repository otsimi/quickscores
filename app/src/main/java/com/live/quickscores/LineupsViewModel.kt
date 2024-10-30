package com.live.quickscores

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.live.quickscores.lineupresponse.LineupsResponse
import kotlinx.coroutines.launch
import retrofit2.Response

class LineupsViewModel(private val repository: LineupsRepository):ViewModel() {
    private val matchLineups=MutableLiveData<Response<LineupsResponse>>()
    val lineups:LiveData<Response<LineupsResponse>>get() = matchLineups

    fun fetchLineups(fixtureId:String){
        viewModelScope.launch {
            try {
                val response=repository.getMatchLineups(fixtureId)
                matchLineups.value=response
                println("${response},Maleenge")
            } catch (e:Exception){
                println(e)
            }
        }
    }
}
class LineupsViewModelFactory(private val repository: LineupsRepository):ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        if (modelClass.isAssignableFrom(LineupsViewModel::class.java)){
            return LineupsViewModel(repository)as T
        }
        throw IllegalArgumentException("Uknown ViewModel scope")
    }
}