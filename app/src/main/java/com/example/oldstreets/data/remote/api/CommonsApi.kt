package com.example.oldstreets.data.remote.api

import com.example.oldstreets.data.remote.dto.commons.CommonsPhotoResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface CommonsApi {

    @GET("w/api.php")
    suspend fun getNearbyPhotos(
        @Query("action") action: String = "query",
        @Query("format") format: String = "json",
        @Query("generator") generator: String = "geosearch",
        @Query("ggscoord") coord: String,
        @Query("ggsradius") radius: Int = 500,
        @Query("ggslimit") limit: Int = 1000,
        @Query("ggsnamespace") namespace: Int = 6,
        @Query("prop") prop: String = "imageinfo",
        @Query("iiprop") iiprop: String = "url"
    ): CommonsPhotoResponse
}