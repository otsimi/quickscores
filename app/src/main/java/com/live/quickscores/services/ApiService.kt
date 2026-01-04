package com.live.quickscores.services

import com.live.quickscores.allleaguesresponse.AllLeaguesReponse
import com.live.quickscores.countriesresponse.CountriesResponse
import com.live.quickscores.fixtureresponse.FixtureResponse
import com.live.quickscores.leagueResponse.LeaguesResponse
import com.live.quickscores.lineupresponse.LineupsResponse
import com.live.quickscores.standingsresponse.StandingsResponse
import com.live.quickscores.teamsresponse.TeamsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("v3/fixtures")
    suspend fun fetchFixtures(@Query("date") date: String): Response<FixtureResponse>

   @GET("v3/fixtures/statistics")
    suspend fun fetchFixtureStatistics(@Query("fixture") fixtureId: String): Response<FixtureResponse>

    @GET("v3/fixtures")
    suspend fun fetchFixturesByTeamId(@Query("id")teamId:String):Response<FixtureResponse>

    @GET("v3/fixtures/lineups")
    suspend fun fetchLineups(@Query("fixture")fixtureId: String):Response<LineupsResponse>

    @GET("/v3/standings")
    suspend fun getLeagueStandings(@Query("league") leagueId: String, @Query("season") season: String): Response<StandingsResponse>

    @GET("v3/countries")
    suspend fun getCountries():Response<CountriesResponse>

    @GET("v3/leagues")
    suspend fun getLeagues(@Query("code") countryCode:String):Response<LeaguesResponse>

    @GET("v3/fixtures")
    suspend fun getFixturesByLeagueId(@Query("league") leagueId:String,@Query("season")season: String,@Query("from") fromDate: String,@Query("to") toDate:String):Response<FixtureResponse>

    @GET("v3/leagues")
    suspend fun getAllLeagues():Response<AllLeaguesReponse>

    @GET("v3/leagues")
    suspend fun fetchLeagues(@Query("type") type: String,@Query("current")current:String?="true"):Response<AllLeaguesReponse>

    @GET("v3/teams")
    suspend fun getTeamsByLeague(@Query("league")leagueId:Int,@Query("season")season: Int):Response<TeamsResponse>
 @GET("v3/fixtures")
 suspend fun getFixturesByIds(@Query("ids") fixtureIds: String): Response<FixtureResponse>


}





