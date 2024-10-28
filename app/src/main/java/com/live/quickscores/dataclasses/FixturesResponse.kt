package com.live.quickscores.dataclasses

data class FixturesResponse(
    val errors: List<Any>,
    val `get`: String,
    val paging: Paging,
    val parameters: Parameters,
    val response: List<FixtureResponses>,
    val results: Int
)