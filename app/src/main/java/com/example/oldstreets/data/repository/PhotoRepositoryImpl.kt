package com.example.oldstreets.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.oldstreets.data.remote.api.PastVuApi
import com.example.oldstreets.domain.model.HistoricalPhoto
import com.example.oldstreets.domain.repository.PhotoDataSource
import com.example.oldstreets.domain.repository.PhotoRepository
import com.squareup.moshi.Moshi
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

private const val MAX_PHOTOS_LIMIT = 20

class PhotoRepositoryImpl @Inject constructor(
    private val dataSources: Set<@JvmSuppressWildcards PhotoDataSource>
) : PhotoRepository {
    override suspend fun getHistoricalPhotos(
        lat: Double,
        lon: Double
    ): Flow<PagingData<HistoricalPhoto>> {
        val pagingSource = dataSources.firstOrNull()?.getHistoricalPhotos(lat, lon)
            ?: return flowOf(PagingData.empty())

        return Pager(
            config = PagingConfig(
                pageSize = 20,
                enablePlaceholders = false,
                initialLoadSize = 40
            )
        ) { pagingSource }.flow
    }
}