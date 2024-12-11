package com.live.quickscores.viewmodelclasses

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.*
import com.live.quickscores.fixtureresponse.FixtureResponse
import com.live.quickscores.repositories.FixturesRepository
import kotlinx.coroutines.launch
import retrofit2.Response

class FixturesViewModel(private val repository: FixturesRepository):ViewModel(){
    private val dailyFixtures = MutableLiveData<Response<FixtureResponse>>()
    val fixtures: LiveData<Response<FixtureResponse>> get() = dailyFixtures

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
