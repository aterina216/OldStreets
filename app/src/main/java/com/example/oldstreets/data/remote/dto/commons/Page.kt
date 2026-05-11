package com.example.oldstreets.data.remote.dto.commons

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Page(
    @Json(name = "pageid") val pageId: Int,
    @Json(name = "ns") val ns: Int,
    @Json(name = "title") val title: String,
    @Json(name = "imageInfo") val imageInfo: List<ImageInfo>?
)