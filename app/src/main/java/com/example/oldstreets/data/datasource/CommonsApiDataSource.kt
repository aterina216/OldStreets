package com.example.oldstreets.data.datasource

import com.example.oldstreets.data.remote.api.CommonsApi
import com.example.oldstreets.data.remote.dto.commons.CommonsPhotoResponse
import com.example.oldstreets.domain.model.HistoricalPhoto
import com.example.oldstreets.domain.repository.PhotoDataSource
import javax.inject.Inject

class CommonsApiDataSource @Inject constructor(
    private val commonsApi: CommonsApi
) : PhotoDataSource {
    override suspend fun getHistoricalPhotos(
        lat: Double,
        lon: Double
    ): List<HistoricalPhoto> {
        val coord = "$lat|$lon"
        val radius = 500
        val limit = 1000

        return try {
            val response = commonsApi.getNearbyPhotos(
                coord = coord, radius = radius, limit = limit
            )
            extractPhotoFromResponse(response)
        }
        catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    private fun extractPhotoFromResponse(response: CommonsPhotoResponse): List<HistoricalPhoto> {
        val pages = response.query.pages
        return pages.values.mapNotNull { page ->
            val imageInfo = page.imageInfo?.firstOrNull()
            if (imageInfo != null && !imageInfo.url.isNullOrEmpty()) {
                HistoricalPhoto(
                    id = page.pageId,
                    title = page.title.removePrefix("File:"),
                    imageUrl = imageInfo.url,
                    year = null
                )
            } else null
        }
    }
}