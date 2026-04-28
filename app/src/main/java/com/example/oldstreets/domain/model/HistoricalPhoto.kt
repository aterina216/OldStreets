package com.example.oldstreets.domain.model

data class HistoricalPhoto (
    val id: String,
    val title: String,
    val imageUrl: String,
    val year: Int?,
    val author: String?
)