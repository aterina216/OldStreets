package com.example.oldstreets.data.repository

import com.example.oldstreets.data.remote.api.PastVuApi
import com.example.oldstreets.domain.model.HistoricalPhoto
import com.example.oldstreets.domain.repository.PhotoDataSource
import com.example.oldstreets.domain.repository.PhotoRepository
import com.squareup.moshi.Moshi
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

private const val MAX_PHOTOS_LIMIT = 20

class PhotoRepositoryImpl @Inject constructor(private val dataSorces: Set<@JvmSuppressWildcards PhotoDataSource>
): PhotoRepository {
    override suspend fun getHistoricalPhotos(
        lat: Double,
        lon: Double
    ): List<HistoricalPhoto> {

        if(dataSorces.isEmpty()) return emptyList()

        return coroutineScope {
            dataSorces.map {
                dataSource ->
                async {
                    try {
                        dataSource.getHistoricalPhotos(lat, lon)
                    }
                    catch (e: Exception) {
                        emptyList()
                    }
                }
            }.flatMap {
                it.await()
            }.distinctBy { it.imageUrl }
        }
    }
}