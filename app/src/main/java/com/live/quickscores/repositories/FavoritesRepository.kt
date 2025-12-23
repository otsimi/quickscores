package com.live.quickscores.repositories

import android.util.Log
import androidx.lifecycle.switchMap
import com.live.quickscores.DataClassFavorite
import com.live.quickscores.FavoriteFixtureEntity
import com.live.quickscores.FavoritesDao
import com.live.quickscores.FixtureEntityDao
import com.live.quickscores.fixtureresponse.Response
import com.live.quickscores.services.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FavoritesRepository(private val favoritesDao: FavoritesDao, private val fixturesDao: FixtureEntityDao, private val api: ApiService
){  fun getAllFavorites() = favoritesDao.getAllFavorites()
    fun observeFavoriteFixtures() = favoritesDao.getAllFavorites().switchMap { favs ->
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
            val response = api.getFixturesByIds(fixtureId.toString())
            println("${fixtureId},fixtureId param from saved favorites")

            println("${response},api response for saved favorites")
            if (!response.isSuccessful) return

            val body = response.body() ?: return

            // Skip if API returned errors
//            if (body.errors != null) {
//                Log.w("FavoritesRepository", "API error for fixture $fixtureId: ${body.errors}")
//                return
//            }

            val fixture = body.response.firstOrNull() ?: return

            val entity = FavoriteFixtureEntity(
                fixtureId = fixture.fixture.id,
                homeTeam = fixture.teams.home.name,
                awayTeam = fixture.teams.away.name,
//                homeGoals = fixture.goals.home?.toString(),
//                awayGoals = fixture.goals.away?.toString(),
                homeLogo = fixture.teams.home.logo,
                awayLogo = fixture.teams.away.logo,
                venue = fixture.fixture.venue.name,
                city = fixture.fixture.venue.city,
                referee = fixture.fixture.referee,
//                homeTeamId = fixture.teams.home.id.toString(),
//                awayTeamId = fixture.teams.away.id.toString(),
                leagueId = fixture.league.id.toString(),
                season = fixture.league.season.toString(),
//                matchPeriod = fixture.fixture.status.elapsed?.toString(),
                country = fixture.league.country,
//                fixtureStatus = fixture.fixture.status.short,
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
                launch { refreshFixture(id) }
            }
            println("${favoriteIds},favoritesId params from saved favorites")
        }
    }

    suspend fun getLiveFavorites(favoriteIds: List<Int>): List<Response> {
        if (favoriteIds.isEmpty()) return emptyList()

        val idsParam = favoriteIds.joinToString("-")
        Log.d("FavoritesRepository", "Fetching live fixtures for IDs: $idsParam")

        return try {
            val response = api.getFixturesByIds(idsParam)

            Log.d("FavoritesRepository", "HTTP code: ${response.code()}")
            Log.d("FavoritesRepository", "Raw response: ${response.raw()}")

            if (!response.isSuccessful) {
                Log.e(
                    "FavoritesRepository",
                    "Error body: ${response.errorBody()?.string()}"
                )
                return emptyList()
            }

            val body = response.body()
            if (body == null) {
                Log.e("FavoritesRepository", "Response body is NULL")
                return emptyList()
            }

            Log.d("FavoritesRepository", "API response size: ${body.response.size}")
            body.response

        } catch (e: Exception) {
            Log.e("FavoritesRepository", "Exception fetching live favorites", e)
            emptyList()
        }
    }


}
