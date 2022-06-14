package com.fabianshallari.pixabay.api

class PixabayApiClient(
    private val pixabayApi: PixabayApi,
    private val apiKey: String
) {
    suspend fun search(searchTerm: String): ApiResult<PixabayImageSearchResult> =
        safeApiCall { pixabayApi.search(searchTerm, apiKey) }

    suspend fun searchById(id: Long): ApiResult<PixabayImageSearchResult> =
        safeApiCall { pixabayApi.searchById(id, apiKey) }
}