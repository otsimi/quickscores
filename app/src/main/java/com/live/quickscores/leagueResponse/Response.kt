package com.live.quickscores.leagueResponse

data class Response(
    val country: Country,
    val league: League,
    val seasons: List<Season>
)