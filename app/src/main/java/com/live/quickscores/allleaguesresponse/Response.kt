package com.live.quickscores.allleaguesresponse

data class Response(
    val country: Country,
    val league: League,
    val seasons: List<Season>
)