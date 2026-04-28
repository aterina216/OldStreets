package com.example.oldstreets.data.remote.api

import com.example.oldstreets.data.remote.dto.AddressSuggestRequest
import com.example.oldstreets.data.remote.dto.AddressSuggestResponse
import com.example.oldstreets.domain.model.Street
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST

interface DataApi {

    @Headers("Content-type: application/json", "Accept: application/json")
    @POST("suggestions/api/4_1/rs/suggest/address")
    suspend fun suggestAddress(
        @Header("Authorization") token: String,
        @Body body: Map<String, @JvmSuppressWildcards Any>
    ): AddressSuggestResponse
}