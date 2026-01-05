package com.live.quickscores.viewmodelclasses

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.live.quickscores.allleaguesresponse.League
import com.live.quickscores.repositories.TeamsRepository
import com.live.quickscores.teamsresponse.Team
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TeamsViewModel(
    private val repository: TeamsRepository
) : ViewModel() {

    private val _teams = MutableStateFlow<Resource<List<Team>>>(Resource.Loading)
    val teams: StateFlow<Resource<List<Team>>> = _teams.asStateFlow()

    private val _leagues = MutableStateFlow<Resource<List<League>>>(Resource.Loading)
    val leagues: StateFlow<Resource<List<League>>> = _leagues.asStateFlow()

//    init {
//        loadPopularLeagues()
//        loadPopularTeams()
//    }

    private fun loadPopularLeagues() {
        viewModelScope.launch {
            _leagues.value = Resource.Loading

            repository.getPopularLeagues()
                .onSuccess {
                    _leagues.value = Resource.Success(it)
                }
                .onFailure {
                    _leagues.value =
                        Resource.Error(it.message ?: "Failed to load leagues")
                }
        }
    }

    fun loadPopularTeams() {
        viewModelScope.launch {
            _teams.value = Resource.Loading

            repository.getTeamsFromPopularLeagues()
                .onSuccess {
                    _teams.value = Resource.Success(it)
                    println("${it},results,Malenge")
                }
                .onFailure {
                    _teams.value =
                        Resource.Error(it.message ?: "Failed to load teams")
                }
        }
    }
}
sealed class Resource<out T> {
    object Loading : Resource<Nothing>()
    data class Success<T>(val data: T) : Resource<T>()
    data class Error(val message: String) : Resource<Nothing>()
}
class TeamsViewModelFactory(
    private val repository: TeamsRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TeamsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return TeamsViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
