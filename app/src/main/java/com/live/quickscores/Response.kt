package com.live.quickscores

import com.live.quickscores.dataclasses.Fixture
import com.live.quickscores.dataclasses.Goals
import com.live.quickscores.dataclasses.League
import com.live.quickscores.dataclasses.Score
import com.live.quickscores.dataclasses.Teams

data class Response(
    val fixture: Fixture,
    val goals: Goals,
    val league: League,
    val score: Score,
    val teams: Teams
)