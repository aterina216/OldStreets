package com.example.oldstreets.domain.repository

import androidx.paging.PagingData
import com.example.oldstreets.domain.model.HistoricalPhoto
import kotlinx.coroutines.flow.Flow

interface PhotoRepository {
    suspend fun getHistoricalPhotos(lat: Double, lon: Double): Flow<PagingData<HistoricalPhoto>>
}