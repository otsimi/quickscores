package com.live.quickscores.viewmodelclasses

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.live.quickscores.AppDatabase
import com.live.quickscores.FavoriteFixtureEntity
import com.live.quickscores.RetrofitClient
import com.live.quickscores.fixtureresponse.Response
import com.live.quickscores.repositories.FavoritesRepository
import kotlinx.coroutines.launch

class FavoriteViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getDatabase(application)
    private val repository = FavoritesRepository(
        favoritesDao = db.favoritesDao(),
        fixturesDao = db.fixtureEntityDao(),
        api = RetrofitClient().getRetrofitInstance()
    )

    val favoriteFixtures: LiveData<List<FavoriteFixtureEntity>> = repository.observeFavoriteFixtures()

    val favoriteIds: LiveData<Set<Int>> = repository.getAllFavorites()
        .map { list -> list.map { it.fixtureId }.toSet() }

    fun toggleFavorite(fixtureId: Int) {
        viewModelScope.launch {
            if (repository.isFavorite(fixtureId)) {
                repository.removeFavorite(fixtureId)
            } else {
                repository.addFavorite(fixtureId)
            }
        }
    }

    fun insertFavoriteFixture(fixture: FavoriteFixtureEntity) {
        viewModelScope.launch {
            repository.insertFixtureEntity(fixture)
        }
    }

    suspend fun isFavorite(fixtureId: Int): Boolean {
        return repository.isFavorite(fixtureId)
    }

    fun refreshFavorites() {
        viewModelScope.launch {
            repository.refreshAllFavorites()
        }
    }

    private val _favoriteLiveFixtures = MutableLiveData<List<Response>>()
    val favoriteLiveFixtures: LiveData<List<Response>> get() = _favoriteLiveFixtures

    fun refreshLiveFavorites(ids: List<Int>) {
        viewModelScope.launch {
            if (ids.isEmpty()) return@launch
            val liveFixtures = repository.getLiveFavorites(ids)
            _favoriteLiveFixtures.postValue(liveFixtures)
            Log.d("FavoriteViewModel", "Fetched ${liveFixtures.size} live fixtures")
        }
    }
}
