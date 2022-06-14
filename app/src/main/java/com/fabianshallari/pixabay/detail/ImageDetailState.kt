package com.fabianshallari.pixabay.detail

import com.fabianshallari.pixabay.api.PixabayImage

data class ImageDetailState(val isLoading: Boolean, val pixabayImage: PixabayImage?)