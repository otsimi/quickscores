package com.live.quickscores.standingsresponse

data class StandingsResponse(
    val errors: List<Any>,
    val `get`: String,
    val paging: Paging,
    val parameters: Parameters,
    val response: List<TableResponse>,
    val results: Int
)