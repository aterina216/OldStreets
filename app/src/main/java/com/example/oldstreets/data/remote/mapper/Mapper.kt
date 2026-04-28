package com.example.oldstreets.data.remote.mapper

import com.example.oldstreets.data.remote.dto.Suggestion
import com.example.oldstreets.domain.model.City
import com.example.oldstreets.domain.model.Street

object Mapper {

    fun Suggestion.toCity(): City? {
        val name = data.city ?: return null
        val fiasId = data.cityFiasId ?: return null
        return City(name, fiasId)
    }

    fun Suggestion.toStreet(): Street? {
        val name = data.street ?: return null
        val fiasId = data.streetFiasId ?: return null
        return Street(name, fiasId)
    }
}