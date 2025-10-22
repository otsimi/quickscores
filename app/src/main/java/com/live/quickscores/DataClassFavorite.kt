package com.live.quickscores

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_matches")
data class DataClassFavorite(
    @PrimaryKey val id: Int,
    val homeTeam: String,
    val awayTeam: String,
    val time: String,
    val homeLogo: String?,
    val awayLogo: String?,
    val league: String?
)
