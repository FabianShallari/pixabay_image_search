package com.fabianshallari.pixabay.api

import retrofit2.http.GET
import retrofit2.http.Query

interface PixabayApi {
    @GET(".")
    suspend fun search(
        @Query("q") searchTerm: String,
        @Query("key") apiKey: String
    ): PixabayImageSearchResult

    @GET(".")
    suspend fun searchById(
        @Query("id") id: Long,
        @Query("key") apiKey: String
    ): PixabayImageSearchResult

}