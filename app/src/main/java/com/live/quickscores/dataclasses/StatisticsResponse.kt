package com.live.quickscores.dataclasses

data class StatisticsResponse(
    val errors: List<Any>,
    val `get`: String,
    val paging: PagingX,
    val parameters: ParametersX,
    val response: List<Response>,
    val results: Int
)