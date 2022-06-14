package com.fabianshallari.pixabay.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fabianshallari.pixabay.api.ApiResult
import com.fabianshallari.pixabay.api.PixabayApiClient
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ImageDetailViewModel @AssistedInject constructor(
    @Assisted private val imageId: Long,
    private val pixabayApiClient: PixabayApiClient
) : ViewModel() {

    private val _state = MutableStateFlow(ImageDetailState(isLoading = true, null))
    private val _actions = Channel<ImageDetailAction>()

    val actions = _actions.receiveAsFlow()
    val state = _state.asStateFlow()

    @AssistedFactory
    fun interface Factory {

        fun create(imageId: Long): ImageDetailViewModel
    }

    init {
        search()
    }

    fun search() {
        viewModelScope.launch {
            _state.update {
                it.copy(isLoading = true)
            }

            when (val result = pixabayApiClient.searchById(imageId)) {
                is ApiResult.Success -> {
                    _state.update {
                        it.copy(isLoading = false, pixabayImage = result.data.images.firstOrNull())
                    }
                }
                is ApiResult.Failure -> {
                    _actions.send(ImageDetailAction.ShowErrorMessage(message = result.error.message ?: "Unknown error"))
                    _state.update {
                        it.copy(isLoading = false)
                    }
                }
            }
        }
    }
}