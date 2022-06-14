package com.fabianshallari.pixabay.api

import android.os.Parcelable
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@JsonClass(generateAdapter = true)
@Parcelize
data class PixabayImage(
    val id: Long,
    val tags: String,
    val user: String,
    val previewURL: String,
    val largeImageURL: String,
    val downloads: Int,
    val likes: Int,
    val comments: Int
) : Parcelable