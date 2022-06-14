package com.fabianshallari.pixabay.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.fabianshallari.pixabay.api.ApiResult
import com.fabianshallari.pixabay.api.PixabayApiClient
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(private val pixabayApiClient: PixabayApiClient) :
    ViewModel() {

    private val _actions = Channel<SearchAction>()

    private val _state = MutableStateFlow(DEFAULT_STATE)

    val state = _state.asStateFlow()
    val actions = _actions.receiveAsFlow()

    init {
        search()
    }

    fun search(searchTerm: String = _state.value.searchTerm) {
        viewModelScope.launch {
            _state.update {
                it.copy(searchTerm = searchTerm, isLoading = true)
            }

            when (val result = pixabayApiClient.search(searchTerm)) {
                is ApiResult.Success -> _state.update {
                    it.copy(images = result.data.images, isLoading = false)
                }
                is ApiResult.Failure -> {
                    _state.update {
                        it.copy(isLoading = false)
                    }
                    _actions.send(
                        SearchAction.ShowErrorMessage(
                            result.error.message ?: "Unknown Error"
                        )
                    )
                }
            }

        }

    }

    fun onItemClicked(position: Int) {
        viewModelScope.launch {
            _state.update {
                it.copy(chosenItem = it.images[position])
            }
            _actions.send(SearchAction.NavigateToConfirmation)
        }
    }

    fun onLastItemChosen() {
        viewModelScope.launch {
            _state.value.chosenItem?.let {
                _actions.send(SearchAction.NavigateToDetail(it))
            }
        }
    }

    companion object {
        val DEFAULT_STATE = SearchState(
            searchTerm = "fruits",
            images = listOf(),
            isLoading = true,
            chosenItem = null,
        )

    }

}