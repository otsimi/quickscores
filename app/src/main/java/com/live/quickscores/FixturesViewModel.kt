package com.live.quickscores

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.live.quickscores.fixturesresponse.FixturesResponse
import androidx.lifecycle.*
import kotlinx.coroutines.launch
import retrofit2.Response

class FixturesViewModel(private val repository: FixturesRepository):ViewModel(){
    private val dailyFixtures = MutableLiveData<Response<FixturesResponse>>()
    val fixtures: LiveData<Response<FixturesResponse>> get() = dailyFixtures

    fun fetchFixtures(date: String) {
        viewModelScope.launch {
            try {
                val response = repository.fetchFixtures(date)
                dailyFixtures.value = response
            } catch (e: Exception) {
                println(e)
            }
        }
    }
}
class FixturesViewModelFactory(private val repository: FixturesRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FixturesViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return FixturesViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
