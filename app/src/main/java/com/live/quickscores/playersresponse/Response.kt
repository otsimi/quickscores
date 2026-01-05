package com.live.quickscores.playersresponse

data class Response(
    val player: Player,
    val statistics: List<Statistic>
)