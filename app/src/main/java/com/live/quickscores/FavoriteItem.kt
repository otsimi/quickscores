package com.live.quickscores

import com.live.quickscores.playersresponse.Player
import com.live.quickscores.teamsresponse.Team

sealed class FavoriteItem {
    data class TeamItem(val team: Team) : FavoriteItem()
    data class PlayerItem(val player: Player) : FavoriteItem()
}