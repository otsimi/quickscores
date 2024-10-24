package com.live.quickscores.dataclasses

data class Response(
    val statistics: List<Statistic>,
    val team: Team
)