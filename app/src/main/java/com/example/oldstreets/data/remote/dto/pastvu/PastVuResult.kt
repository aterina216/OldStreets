package com.example.oldstreets.data.remote.dto.pastvu

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PastVuResult(
    val photos: List<PastVuPhoto>
)