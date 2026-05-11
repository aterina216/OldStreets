package com.example.oldstreets.data.remote.dto.pastvu

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PastVuPhoto (
    val cid: Int,
    val title: String,
    val year: String?,
    val file: String
)