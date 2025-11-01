package com.live.quickscores

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query


@Dao
interface FavoritesDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavorite(match: DataClassFavorite)

    @Delete
    suspend fun deleteFavorite(match: DataClassFavorite)

    @Query("SELECT * FROM favorite_matches")
    fun getAllFavorites(): LiveData<List<DataClassFavorite>>

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_matches WHERE id = :matchId)")
    suspend fun isFavorite(matchId: Int): Boolean
}