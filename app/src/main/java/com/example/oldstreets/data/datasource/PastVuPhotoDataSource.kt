package com.example.oldstreets.data.datasource

import com.example.oldstreets.data.remote.api.PastVuApi
import com.example.oldstreets.domain.model.HistoricalPhoto
import com.example.oldstreets.domain.repository.PhotoDataSource
import com.squareup.moshi.Moshi
import javax.inject.Inject

class PastVuPhotoDataSource @Inject constructor(
    private val api: PastVuApi,
    private val moshi: Moshi
): PhotoDataSource {
    override suspend fun getHistoricalPhotos(
        lat: Double,
        lon: Double
    ): List<HistoricalPhoto> {
        val params = mapOf(
            "geo" to listOf(lat, lon),
            "distance" to 2000,
            "limit" to 1000
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