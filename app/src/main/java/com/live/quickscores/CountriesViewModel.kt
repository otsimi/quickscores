package com.live.quickscores

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.live.quickscores.countriesresponse.CountriesResponse
import kotlinx.coroutines.launch
import retrofit2.Response

class CountriesViewModel(private val repository: CountriesRepository):ViewModel() {
    private val countries=MutableLiveData<Response<CountriesResponse>>()
    val allCountries:LiveData<Response<CountriesResponse>>get() = countries

    fun fetchCountries(){
        viewModelScope.launch {
            try {
                val response=repository.fetchCountries()
                countries.value=response
                println("${response},countries response")
            }catch (
                e:Exception
            ){
                Log.e("e","Error exception")
            }
        }
    }
}
class CountriesViewModelFactory(private val repository: CountriesRepository):ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        if (modelClass.isAssignableFrom(CountriesViewModel::class.java)) {
            return CountriesViewModel(repository) as T
        }
        throw IllegalArgumentException("Uknown ViewModel scope")
    }
}