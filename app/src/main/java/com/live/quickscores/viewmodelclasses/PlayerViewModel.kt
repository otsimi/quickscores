package com.live.quickscores.viewmodelclasses

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.live.quickscores.playersresponse.Player
import com.live.quickscores.repositories.PlayerRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PlayerViewModel(private val repository: PlayerRepository): ViewModel() {
    private val _players = MutableStateFlow<Resource<List<Player>>>(Resource.Loading)
    val players: StateFlow<Resource<List<Player>>> = _players.asStateFlow()

    fun loadPopularPlayers() {
        viewModelScope.launch {
            _players.value = Resource.Loading
            repository.getPopularPlayers()
                .onSuccess { _players.value = Resource.Success(it)
                    println("${it},players, Malenge")
                }

                .onFailure {
                    _players.value = Resource.Error(it.message ?: "Failed to load players")
                }
        }
    }
}
class PlayersViewModelFactory(
    private val repository: PlayerRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PlayerViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return PlayerViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}