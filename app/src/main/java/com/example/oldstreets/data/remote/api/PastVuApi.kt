package com.example.oldstreets.data.remote.api


import com.example.oldstreets.data.remote.dto.PastVuResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface PastVuApi {
    @GET("api2")
    suspend fun getNearestPhotos(
        @Query("method") method: String = "photo.giveNearestPhotos",
        @Query("params") paramsJson: String
    ): PastVuResponse
}