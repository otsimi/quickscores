package com.live.quickscores.lineupresponse

data class Response(
    val coach: Coach,
    val formation: String,
    val startXI: List<StartXI>,
    val substitutes: List<Substitute>,
    val team: Team
)