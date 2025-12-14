package com.live.quickscores

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "favorite_matches")
data class DataClassFavorite(
    @PrimaryKey val fixtureId: Int
)
