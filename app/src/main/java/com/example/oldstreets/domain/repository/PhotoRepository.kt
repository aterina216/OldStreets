package com.example.oldstreets.domain.repository

import com.example.oldstreets.domain.model.HistoricalPhoto

interface PhotoRepository {
    suspend fun getHistoricalPhotos(lat: Double, lon: Double): List<HistoricalPhoto>
}