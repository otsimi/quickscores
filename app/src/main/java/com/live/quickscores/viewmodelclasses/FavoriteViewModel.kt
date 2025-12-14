package com.live.quickscores.viewmodelclasses

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.live.quickscores.AppDatabase
import com.live.quickscores.FavoriteFixtureEntity
import com.live.quickscores.RetrofitClient
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
    val favoriteIds: LiveData<Set<Int>> = repository.getAllFavorites().map { list -> list.map { it.fixtureId }.toSet() }

}
