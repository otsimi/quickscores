package com.live.quickscores.repositories

import com.live.quickscores.DataClassFavorite
import com.live.quickscores.FavoritesDao

class FavoritesRepository(private val dao: FavoritesDao) {
    fun getAllFavorites() = dao.getAllFavorites()
    suspend fun addFavorite(match: DataClassFavorite) = dao.insertFavorite(match)
    suspend fun removeFavorite(match: DataClassFavorite) = dao.deleteFavorite(match)

    suspend fun isFavorite(matchId: Int) = dao.isFavorite(matchId)
}