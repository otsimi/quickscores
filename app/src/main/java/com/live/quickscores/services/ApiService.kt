package com.live.quickscores.services

import com.live.quickscores.allleaguesresponse.AllLeaguesReponse
import com.live.quickscores.countriesresponse.CountriesResponse
import com.live.quickscores.fixturesresponse.FixturesResponse
import com.live.quickscores.fixturesresponse.statisticsResponse.StatisticsResponse
import com.live.quickscores.leagueResponse.LeaguesResponse
import com.live.quickscores.lineupresponse.LineupsResponse
import com.live.quickscores.standingsresponse.StandingsResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("v3/fixtures")
    suspend fun fetchFixtures(@Query("date") date: String): Response<FixturesResponse>

   @GET("v3/fixtures/statistics")
    suspend fun fetchFixtureStatistics(@Query("fixture") fixtureId: String): Response<StatisticsResponse>

    @GET("v3/fixtures")
    suspend fun fetchFixturesByTeamId(@Query("id")teamId:String):Response<FixturesResponse>

    @GET("v3/fixtures/lineups")
    suspend fun fetchLineups(@Query("fixture")fixtureId: String):Response<LineupsResponse>

    @GET("/v3/standings")
    suspend fun getLeagueStandings(@Query("league") leagueId: String, @Query("season") season: String): Response<StandingsResponse>

    @GET("v3/countries")
    suspend fun getCountries():Response<CountriesResponse>

    @GET("v3/leagues")
    suspend fun getLeagues(@Query("code") countryCode:String):Response<LeaguesResponse>

    @GET("v3/fixtures")
    suspend fun getFixturesByLeagueId(@Query("league") leagueId:String,@Query("season")season: String,@Query("from") fromDate: String,@Query("to") toDate:String):Response<FixturesResponse>

    @GET("v3/leagues")
    suspend fun getAllLeagues():Response<AllLeaguesReponse>

}





