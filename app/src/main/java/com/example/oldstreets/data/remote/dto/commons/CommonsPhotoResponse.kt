package com.example.oldstreets.data.remote.dto.commons

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CommonsPhotoResponse(
    @Json(name = "query") val query: Query
)