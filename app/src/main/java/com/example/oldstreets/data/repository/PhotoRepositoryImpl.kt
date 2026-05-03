package com.example.oldstreets.data.repository

import com.example.oldstreets.data.remote.api.PastVuApi
import com.example.oldstreets.domain.model.HistoricalPhoto
import com.example.oldstreets.domain.repository.PhotoRepository
import com.squareup.moshi.Moshi
import javax.inject.Inject

private const val MAX_PHOTOS_LIMIT = 20

class PhotoRepositoryImpl @Inject constructor(private val api: PastVuApi,
                                              private val moshi: Moshi): PhotoRepository {
    override suspend fun getHistoricalPhotos(
        lat: Double,
        lon: Double
    ): List<HistoricalPhoto> {
        val params = mapOf(
            "geo" to listOf(lat, lon),
            "distance" to 2000,
            "limit" to MAX_PHOTOS_LIMIT
        )

        val paramsJson = moshi.adapter(Map::class.java).toJson(params)

        val response = api.getNearestPhotos(paramsJson = paramsJson)
        return response.result.photos.map { photo ->
            HistoricalPhoto(
                id = photo.cid,
                title = photo.title,
                imageUrl = "https://img.pastvu.com/d/${photo.file}",
                year = photo.year?.toIntOrNull()
            )
        }
    }
}