package com.live.quickscores.fixturesresponse

data class FixturesResponse(
    val errors: List<Any>,
    val `get`: String,
    val paging: Paging,
    val parameters: Parameters,
    val response: List<Response>,
    val results: Int
)