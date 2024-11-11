package com.live.quickscores.allleaguesresponse

data class AllLeaguesReponse(
    val errors: List<Any>,
    val `get`: String,
    val paging: Paging,
    val parameters: List<Any>,
    val response: List<Response>,
    val results: Int
)