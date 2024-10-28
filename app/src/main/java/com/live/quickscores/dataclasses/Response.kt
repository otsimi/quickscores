package com.live.quickscores.dataclasses

data class FixtureResponses(
    val fixture: Fixture,
    val goals: Goals,
    val league: League,
    val score: Score,
    val teams: Teams
)