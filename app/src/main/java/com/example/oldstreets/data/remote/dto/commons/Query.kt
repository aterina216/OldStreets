package com.example.oldstreets.data.remote.dto.commons

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Query(
    @Json(name = "pages") val pages: Map<String, Page>
)