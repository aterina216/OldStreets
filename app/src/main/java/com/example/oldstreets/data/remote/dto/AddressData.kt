package com.example.oldstreets.data.remote.dto
import com.squareup.moshi.Json
data class AddressData (
    @Json(name = "city") val city: String?,
    @Json(name = "city_fias_id") val cityFiasId: String?,
    @Json(name = "street") val street: String?,
    @Json(name = "street_fias_id") val streetFiasId: String?,
    @Json(name = "geo_lat") val lat: String?,
    @Json(name = "geo_lon") val lon: String?
)