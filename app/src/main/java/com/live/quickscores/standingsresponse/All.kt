package com.live.quickscores.standingsresponse

data class All(
    val draw: Int,
    val goals: Goals,
    val lose: Int,
    val played: Int,
    val win: Int
)