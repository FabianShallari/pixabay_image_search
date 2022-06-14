package com.fabianshallari.pixabay.search

import com.fabianshallari.pixabay.api.PixabayImage

sealed class SearchAction {
    data class ShowErrorMessage(val message: String) : SearchAction()
    data class NavigateToDetail(val pixabayImage: PixabayImage) : SearchAction()
    object NavigateToConfirmation: SearchAction()
}