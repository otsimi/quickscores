package com.live.quickscores.dataclasses.statisticsResponse

data class StatisticsResponse(
    val errors: List<Any>,
    val `get`: String,
    val paging: Paging,
    val parameters: Parameters,
    val response: List<Response>,
    val results: Int
)