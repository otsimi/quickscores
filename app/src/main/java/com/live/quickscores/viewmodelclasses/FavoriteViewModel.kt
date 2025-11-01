package com.live.quickscores.viewmodelclasses

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.live.quickscores.AppDatabase
import com.live.quickscores.DataClassFavorite
import com.live.quickscores.repositories.FavoritesRepository
import kotlinx.coroutines.launch

class FavoriteViewModel(application: Application): AndroidViewModel(application) {
    private val dao = AppDatabase.getDatabase(application).favoritesDao()
    private val repository= FavoritesRepository(dao)
    val allFavorites : LiveData<List<DataClassFavorite>> =repository.getAllFavorites()

    fun saveFavorite(match: DataClassFavorite){
        viewModelScope.launch {
            if (repository.isFavorite(match.id)){
                repository.removeFavorite(match)
            } else{
                repository.addFavorite(match)
            }
        }
    }
}