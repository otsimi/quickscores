package com.live.quickscores.countriesresponse

data class CountriesResponse(
    val errors: List<Any>,
    val `get`: String,
    val paging: Paging,
    val parameters: List<Any>,
    val response: List<Response>,
    val results: Int
)