package com.example.oldstreets.data.remote.dto.pastvu

import com.example.oldstreets.data.remote.dto.pastvu.PastVuResult
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PastVuResponse (
    val result: PastVuResult
)