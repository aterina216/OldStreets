package com.example.oldstreets.data.datasource

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
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
    ): PagingSource<Int, HistoricalPhoto> {

        return object : PagingSource<Int, HistoricalPhoto>() {
            override fun getRefreshKey(state: PagingState<Int, HistoricalPhoto>): Int? {
                return null
            }

            override suspend fun load(params: LoadParams<Int>): LoadResult<Int, HistoricalPhoto> {
                Log.d("CommonsPaging", "load called, key=${params.key}, loadSize=${params.loadSize}")
                return try {
                    val coord = "$lat|$lon"
                    val radius = 500
                    val pageSize = params.loadSize
                    val page = params.key ?: 1
                    val offset = (page - 1) * pageSize

                    Log.d("PhotoSource", "Commons: загружаю фото для $lat, $lon, страница $page")

                    val response = commonsApi.getNearbyPhotos(
                        coord = coord, radius = radius, limit = pageSize
                    )
                    val photos = extractPhotoFromResponse(response)
                    Log.d("CommonsPaging", "Loaded ${photos.size} photos, page=$page")

                    val nextKey = if (photos.size == pageSize) page + 1 else null
                    LoadResult.Page(
                        data = photos,
                        prevKey = if(page > 1) page - 1 else null,
                        nextKey = nextKey
                    )
                }
                catch (e: Exception) {
                    LoadResult.Error(e)
                }
            }

        }
    }

    private fun extractPhotoFromResponse(response: CommonsPhotoResponse): List<HistoricalPhoto> {
        val pages = response.query?.pages ?: emptyMap()
        Log.d("CommonsPaging", "Pages count: ${pages.size}")
        val result = pages.values.mapNotNull { page ->
            val imageInfo = page.imageInfo?.firstOrNull()
            if (imageInfo != null && !imageInfo.url.isNullOrEmpty()) {
                HistoricalPhoto(
                    id = page.pageId,
                    title = page.title.removePrefix("File:"),
                    imageUrl = imageInfo.url,
                    year = null
                )
            } else{
                Log.d("CommonsPaging", "Skipping page ${page.pageId}: imageInfo=${imageInfo != null}, url=${imageInfo?.url}")
                null
            }
        }
        Log.d("CommonsPaging", "Result size: ${result.size}")
        return result
    }
}