package com.live.quickscores.standingsresponse

data class League(
    val country: String,
    val flag: String,
    val id: Int,
    val logo: String,
    val name: String,
    val season: Int,
    val standings: List<Standing>
)