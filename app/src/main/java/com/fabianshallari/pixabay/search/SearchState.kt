package com.fabianshallari.pixabay.search

import com.fabianshallari.pixabay.api.PixabayImage

data class SearchState(
    val searchTerm: String,
    val images: List<PixabayImage>,
    val chosenItem: PixabayImage?,
    val isLoading: Boolean
)

