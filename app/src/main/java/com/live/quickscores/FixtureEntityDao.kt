package com.live.quickscores

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface FixtureEntityDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFixtures(fixtures: List<FavoriteFixtureEntity>)

    @Query("SELECT * FROM fixtures_entity WHERE fixtureId IN (:matchIds)")
    fun getFixturesByMatchId(matchIds: List<Int>): LiveData<List<FavoriteFixtureEntity>>

    @Query("""
        SELECT f.* FROM fixtures_entity f
        INNER JOIN favorite_matches fav
        ON f.fixtureId = fav.fixtureId
    """)
    fun observeFavoriteFixtures(): LiveData<List<FavoriteFixtureEntity>>


}