package com.live.quickscores.repositories

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.switchMap
import com.live.quickscores.DataClassFavorite
import com.live.quickscores.FavoriteFixtureEntity
import com.live.quickscores.FavoritesDao
import com.live.quickscores.FixtureEntityDao
import com.live.quickscores.services.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FavoritesRepository(
    private val favoritesDao: FavoritesDao,
    private val fixturesDao: FixtureEntityDao,
    private val api: ApiService
) {

    // Observe IDs of favorites (LiveData)
    fun getAllFavorites(): LiveData<List<DataClassFavorite>> = favoritesDao.getAllFavorites()

    // Observe full fixture entities of favorites (LiveData)
    fun observeFavoriteFixtures(): LiveData<List<FavoriteFixtureEntity>> =
        favoritesDao.getAllFavorites().switchMap { favs ->
            fixturesDao.getFixturesByMatchId(favs.map { it.fixtureId })

        }

    suspend fun addFavorite(fixtureId: Int) {
        favoritesDao.insertFavorite(DataClassFavorite(fixtureId))
        refreshFixture(fixtureId)
    }

    suspend fun removeFavorite(fixtureId: Int) {
        favoritesDao.deleteFavorite(DataClassFavorite(fixtureId))
    }

    suspend fun isFavorite(fixtureId: Int): Boolean {
        return favoritesDao.isFavorite(fixtureId)
    }

    suspend fun insertFixtureEntity(fixture: FavoriteFixtureEntity) {
        fixturesDao.insertFixtures(listOf(fixture))
    }

    private suspend fun refreshFixture(fixtureId: Int) {
        try {
            val response = api.getFixturesByFixtureId(fixtureId)
            if (!response.isSuccessful) return

            val fixture = response.body()?.response?.firstOrNull() ?: return

            val entity = FavoriteFixtureEntity(
                fixtureId = fixture.fixture.id,
                homeTeam = fixture.teams.home.name,
                awayTeam = fixture.teams.away.name,
                homeGoals = fixture.goals.home.toString(),
                awayGoals = fixture.goals.away.toString(),
                homeLogo = fixture.teams.home.logo,
                awayLogo = fixture.teams.away.logo,
                venue = fixture.fixture.venue.name,
                city = fixture.fixture.venue.city,
                referee = fixture.fixture.referee,
                homeTeamId = fixture.teams.home.id.toString(),
                awayTeamId = fixture.teams.away.id.toString(),
                leagueId = fixture.league.id.toString(),
                season = fixture.league.season.toString(),
                matchPeriod = fixture.fixture.status.elapsed?.toString(),
                country = fixture.league.country,
                fixtureStatus = fixture.fixture.status.short,
                time = fixture.fixture.date
            )

            fixturesDao.insertFixtures(listOf(entity))

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun refreshAllFavorites() = withContext(Dispatchers.IO) {
        val favoriteIds = favoritesDao.getFavoritesOnce().map { it.fixtureId }

        coroutineScope {
            favoriteIds.forEach { id ->
                launch {
                    refreshFixture(id)
                }
            }
        }
    }


}
