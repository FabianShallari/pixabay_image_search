package com.fabianshallari.pixabay.detail

sealed class ImageDetailAction {
    data class ShowErrorMessage(val message: String): ImageDetailAction()
}