package com.example.oldstreets.domain.repository

import androidx.paging.PagingSource
import com.example.oldstreets.domain.model.HistoricalPhoto

interface PhotoDataSource {
    suspend fun getHistoricalPhotos(lat: Double, lon: Double): PagingSource<Int, HistoricalPhoto>
}