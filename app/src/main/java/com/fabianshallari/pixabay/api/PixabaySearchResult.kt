package com.fabianshallari.pixabay.api

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PixabayImageSearchResult(
    val total: Int,
    @Json(name = "hits") val images: List<PixabayImage>
)