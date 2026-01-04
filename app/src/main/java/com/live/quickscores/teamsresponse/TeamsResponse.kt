package com.live.quickscores.teamsresponse

data class TeamsResponse(
    val errors: List<Any>,
    val `get`: String,
    val info: String,
    val paging: Paging,
    val parameters: Parameters,
    val response: List<Response>,
    val results: Int
)