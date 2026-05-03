package com.example.oldstreets.data.remote.dto

import com.squareup.moshi.JsonClass
@JsonClass(generateAdapter = true)
data class PastVuResponse (
    val result: PastVuResult
)