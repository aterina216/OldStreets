package com.example.oldstreets.data.datasource

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
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
    ): PagingSource<Int, HistoricalPhoto> {

        return object : PagingSource<Int, HistoricalPhoto>() {
            override fun getRefreshKey(state: PagingState<Int, HistoricalPhoto>): Int? {
                return state.anchorPosition?.let {
                    anchorPosition ->
                    state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                }
            }

            override suspend fun load(params: LoadParams<Int>): LoadResult<Int, HistoricalPhoto> {
               val page = params.key ?: 1
                Log.d("PAGING", "Загружаю страницу $page, размер: ${params.loadSize}")
                Log.d("PhotoSource", "PastVu: загружаю фото для $lat, $lon, страница $page")
                val pageSize = params.loadSize
                val offset = (page - 1) * pageSize

                val paramJson = buildPastVuParams(lat, lon, offset, pageSize)
                Log.d("PAGING", "PastVu request: $paramJson")
                val response = api.getNearestPhotos(paramsJson = paramJson)
                Log.d("PAGING", "PastVu response photos count: ${response.result.photos.size}")

                val photos = response.result.photos.map {
                    pastVuPhoto ->
                    HistoricalPhoto(
                        id = pastVuPhoto.cid,
                        title = pastVuPhoto.title,
                        imageUrl = "https://img.pastvu.com/d/${pastVuPhoto.file}",
                        year = pastVuPhoto.year?.toIntOrNull()
                    )
                }
                val nextKey = if (photos.size == pageSize) page + 1 else null

                return LoadResult.Page(
                    data = photos,
                    prevKey = if(page > 1) page - 1 else null,
                    nextKey
                )
            }
        }
    }

    private fun buildPastVuParams(lat: Double, lon: Double, offset: Int, limit: Int): String {
        val params = mapOf(
            "geo" to listOf(lat, lon),
            "distance" to 2000,
            "offset" to offset,
            "limit" to limit
        )
        return moshi.adapter(Map::class.java).toJson(params)
    }
}