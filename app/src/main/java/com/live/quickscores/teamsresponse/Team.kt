package com.live.quickscores.teamsresponse

data class Team(
    val code: String,
    val country: String,
    val founded: Int,
    val id: Int,
    val logo: String,
    val name: String,
    val national: Boolean
)