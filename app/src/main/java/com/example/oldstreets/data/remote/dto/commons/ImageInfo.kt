package com.example.oldstreets.data.remote.dto.commons

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ImageInfo(
    @Json(name = "thumburl") val thumbUrl: String?,
    @Json(name = "url") val url: String,
    @Json(name = "descriptionurl") val descriptionUrl: String
)