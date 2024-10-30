package com.live.quickscores.standingsresponse

data class Away(
    val draw: Int,
    val goals: Goals,
    val lose: Int,
    val played: Int,
    val win: Int
)