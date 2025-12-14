package com.live.quickscores.fixtureresponse

import androidx.room.Entity

@Entity(tableName = "fixtures_entity")
data class FixtureResponse(
    val errors: List<Any>,
    val `get`: String,
    val paging: Paging,
    val parameters: Parameters,
    val response: List<Response>,
    val results: Int
)