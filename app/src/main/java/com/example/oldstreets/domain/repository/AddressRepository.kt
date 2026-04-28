package com.example.oldstreets.domain.repository

import com.example.oldstreets.domain.model.City
import com.example.oldstreets.domain.model.Street

interface AddressRepository {

    suspend fun suggestSities(query: String): List<City>
    suspend fun suggestStreets(cityFastId: String, query: String): List<Street>
    suspend fun getCoordinates(streetFastId: String): Pair<Double, Double>?
}