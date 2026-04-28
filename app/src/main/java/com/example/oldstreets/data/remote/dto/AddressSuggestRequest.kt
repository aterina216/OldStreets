package com.example.oldstreets.data.remote.dto

data class AddressSuggestRequest(
    val query: String,
    val from_bound: Bound,
    val to_bound: Bound,
    val locations: List<Location>? = null
)

data class Bound(val value: String)
data class Location(val city_fias_id: String)