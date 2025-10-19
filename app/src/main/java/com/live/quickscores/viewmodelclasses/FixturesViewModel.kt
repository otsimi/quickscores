package com.live.quickscores.viewmodelclasses

import androidx.lifecycle.*
import com.live.quickscores.fixtureresponse.FixtureResponse
import com.live.quickscores.repositories.FixturesRepository
import kotlinx.coroutines.launch
import retrofit2.Response

class FixturesViewModel(private val repository: FixturesRepository) : ViewModel() {

    private val dailyFixtures = MutableLiveData<Response<FixtureResponse>>()
    val fixtures: LiveData<Response<FixtureResponse>> get() = dailyFixtures

    fun fetchFixtures(date: String) {
        viewModelScope.launch {
            try {
                val response = repository.fetchFixtures(date)
                if (response.isSuccessful && response.body() != null) {

                    val fixtureResponse = response.body()!!
                    val sortedFixtures = sortFixturesByCountryAndLeaguePriority(fixtureResponse.response)
                    val sortedFixtureResponse = fixtureResponse.copy(response = sortedFixtures)
                    dailyFixtures.value = Response.success(sortedFixtureResponse)

                } else {
                    dailyFixtures.value = response
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    private fun sortFixturesByCountryAndLeaguePriority(
        fixtures: List<com.live.quickscores.fixtureresponse.Response>
    ): List<com.live.quickscores.fixtureresponse.Response> {
        val topWorldCompetitions = listOf(
            "UEFA Champions League",
            "UEFA Europa League",
            "UEFA Conference League"
        )
        val majorCupCompetitions = mapOf(
            "England" to listOf("FA Cup", "EFL Cup", "Carabao Cup", "Community Shield"),
            "Spain" to listOf("Copa del Rey", "Super Cup")
        )
        val countryPriority = listOf(
            "England", "Spain", "Germany", "Italy", "France", "Netherlands", "Portugal"
        )
        val leaguePriorityMap = mapOf(
            "England" to listOf("Premier League", "Championship", "League One", "League Two", "National League"),
            "Spain" to listOf("La Liga", "La Liga 2", "Primera Federación"),
            "Germany" to listOf("Bundesliga", "2. Bundesliga", "3. Liga"),
            "Italy" to listOf("Serie A", "Serie B", "Serie C"),
            "France" to listOf("Ligue 1", "Ligue 2"),
            "Netherlands" to listOf("Eredivisie", "Eerste Divisie"),
            "Portugal" to listOf("Primeira Liga", "Liga Portugal 2")
        )
        return fixtures.sortedWith(
            compareBy<com.live.quickscores.fixtureresponse.Response> { fixture ->
                val isWorld = fixture.league.country.equals("World", ignoreCase = true)
                val worldRank = if (isWorld && topWorldCompetitions.contains(fixture.league.name)) 0 else Int.MAX_VALUE
                println("🌍 ${fixture.league.name} (${fixture.league.country}) → worldRank=$worldRank")
                worldRank
            }.thenBy { fixture ->

                val leagueName = fixture.league.name
                val country = fixture.league.country

                val isTopNationalLeague =
                    (country == "England" && leagueName == "Premier League") || (country == "Spain" && leagueName == "La Liga")||(country=="Germany"&& leagueName=="Bundesliga")

                val nationalLeagueRank = if (isTopNationalLeague) 1 else Int.MAX_VALUE
                println("🇬🇧 ${fixture.league.country} - ${fixture.league.name} → topNationalRank=$nationalLeagueRank")
                nationalLeagueRank
            }.thenBy { fixture ->

                val cups = majorCupCompetitions[fixture.league.country]
                val cupRank = if (cups?.contains(fixture.league.name) == true) 2 else Int.MAX_VALUE
                println("🏆 ${fixture.league.country} - ${fixture.league.name} → cupRank=$cupRank")
                cupRank
            }.thenBy { fixture ->

                val countryIndex = countryPriority.indexOf(fixture.league.country)
                val countryRank = if (countryIndex == -1) Int.MAX_VALUE else countryIndex
                println("🌍 ${fixture.league.country} (${fixture.league.name}) → countryRank=$countryRank")
                countryRank
            }.thenBy { fixture ->

                val leagueList = leaguePriorityMap[fixture.league.country]
                val leagueIndex = leagueList?.indexOf(fixture.league.name) ?: Int.MAX_VALUE
                val leagueRank = if (leagueIndex == -1) Int.MAX_VALUE else leagueIndex
                println("⚽ ${fixture.league.country} - ${fixture.league.name} → leagueRank=$leagueRank")
                leagueRank
            }.thenBy { fixture ->
                fixture.league.name
            }
        )
    }



}

class FixturesViewModelFactory(private val repository: FixturesRepository) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FixturesViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return FixturesViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
