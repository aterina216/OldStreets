package com.example.oldstreets.data.repository

import android.util.Log
import com.example.oldstreets.data.remote.api.ApiKey
import com.example.oldstreets.data.remote.api.DataApi
import com.example.oldstreets.domain.model.City
import com.example.oldstreets.domain.model.Street
import com.example.oldstreets.domain.repository.AddressRepository
import javax.inject.Inject

class AdressRepositoryImpl @Inject constructor
    (private val api: DataApi): AddressRepository {
    override suspend fun suggestSities(query: String): List<City> {
        Log.d("DaData", "=== suggestCities CALLED with query: $query ===")
        val body = mapOf(
            "query" to query,
            "from_bound" to mapOf("value" to "city"),
            "to_bound" to mapOf("value" to "city")
        )
        val response = api.suggestAddress("Token ${ApiKey.API_KEY}", body)
        Log.d("DaData", "Response suggestions count: ${response.suggestions.size}")
        return response.suggestions.mapNotNull {
            suggestion ->
            suggestion.data.city?.let {
                cityName ->
                suggestion.data.cityFiasId?.let {
                    fiasId ->
                    City(cityName, fiasId)
                }
            }
        }.distinctBy { it.fasId }
    }

    override suspend fun suggestStreets(
        cityFastId: String,
        query: String
    ): List<Street> {
        val body = mapOf(
            "query" to query,
            "locations" to listOf(mapOf("city_fias_id" to cityFastId)),
            "from_bound" to mapOf("value" to "street"),
            "to_bound" to mapOf("value" to "street")
        )
        val response = api.suggestAddress("Token ${ApiKey.API_KEY}", body)
        return response.suggestions.mapNotNull { suggestion ->
            suggestion.data.street?.let {
                streetName ->
                suggestion.data.streetFiasId?.let {
                    fiasId ->
                    Street(streetName, fiasId)
                }
            }
        }.distinctBy {
            it.fiasId
        }
    }

    override suspend fun getCoordinates(streetFastId: String): Pair<Double, Double>? {
        val body = mapOf(
            "query" to streetFastId,
            "from_bound" to mapOf("value" to "street"),
            "to_bound" to mapOf("value" to "street")
        )

        val response = api.suggestAddress("Token ${ApiKey.API_KEY}", body)
        val suggestion = response.suggestions.firstOrNull()
        val lat = suggestion?.data?.lat?.toDoubleOrNull()
        val lon = suggestion?.data?.lon?.toDoubleOrNull()
        return if (lat != null && lon != null) lat to lon else null
    }

}