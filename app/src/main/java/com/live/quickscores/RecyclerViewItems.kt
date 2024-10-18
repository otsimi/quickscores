package com.live.quickscores

sealed class RecyclerViewItems {
    data class Header(val leagueName: String, val countryName: String) : RecyclerViewItems()
    data class Match(val fixtureResponse: Response) : RecyclerViewItems()

}