package com.fabianshallari.pixabay

import app.cash.turbine.test
import com.fabianshallari.pixabay.api.ApiResult
import com.fabianshallari.pixabay.api.PixabayApiClient
import com.fabianshallari.pixabay.api.PixabayImage
import com.fabianshallari.pixabay.api.PixabayImageSearchResult
import com.fabianshallari.pixabay.search.SearchAction
import com.fabianshallari.pixabay.search.SearchState
import com.fabianshallari.pixabay.search.SearchViewModel
import com.google.common.truth.Truth.assertThat
import com.nhaarman.mockitokotlin2.given
import com.nhaarman.mockitokotlin2.mock
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SearchViewModelTest {

    @get:Rule
    val testRule = CoroutinesTestRule(StandardTestDispatcher())

    private val pixabayApiClient: PixabayApiClient = mock()
    private val searchViewModel: SearchViewModel by lazy { SearchViewModel(pixabayApiClient) }

    @Test
    fun `SHOULD start with correct default state`() = runTest {
        val defaultState = searchViewModel.state.value
        assertThat(defaultState).isEqualTo(SearchViewModel.DEFAULT_STATE)
    }

    @Test
    fun `SHOULD search for fruits WHEN it is created`() = runTest {
        given(pixabayApiClient.search("fruits")).willReturn(ApiResult.Success(IMAGE_SEARCH_RESULT))

        searchViewModel
            .state
            .test {
                skipItems(1)

                val state = awaitItem()

                assertThat(state).isEqualTo(
                    SearchState(
                        searchTerm = "fruits",
                        images = listOf(TEST_IMAGE),
                        isLoading = false,
                        chosenItem = null
                    )
                )
            }
    }

    @Test
    fun `SHOULD show an error WHEN api returns a failure result`() = runTest {
        given(pixabayApiClient.search("fruits")).willReturn(ApiResult.Failure(RuntimeException("Error")))

        searchViewModel
            .state
            .test {
                skipItems(1)

                val state = awaitItem()

                assertThat(state).isEqualTo(
                    SearchState(
                        searchTerm = searchViewModel.state.value.searchTerm,
                        images = listOf(),
                        isLoading = false,
                        chosenItem = null
                    )
                )
            }

        searchViewModel
            .actions
            .test {
                val action = awaitItem()

                assertThat(action).isEqualTo(SearchAction.ShowErrorMessage("Error"))
            }
    }

    @Test
    fun `SHOULD select correct item AND send correct navigation event WHEN item is clicked`() =
        runTest {
            given(pixabayApiClient.search("fruits")).willReturn(
                ApiResult.Success(
                    IMAGE_SEARCH_RESULT
                )
            )


            searchViewModel
                .state
                .test {
                    searchViewModel.onItemClicked(0)

                    skipItems(2)

                    val state = awaitItem()

                    assertThat(state).isEqualTo(
                        SearchState(
                            searchTerm = searchViewModel.state.value.searchTerm,
                            images = listOf(TEST_IMAGE),
                            isLoading = false,
                            chosenItem = TEST_IMAGE
                        )
                    )
                }

            searchViewModel
                .actions
                .test {
                    val action = awaitItem()
                    assertThat(action).isEqualTo(
                        SearchAction.NavigateToConfirmation
                    )
                }
        }

    @Test
    fun `SHOULD send correct navigation event WHEN last item is chosen`() = runTest {
        given(pixabayApiClient.search("fruits")).willReturn(ApiResult.Success(IMAGE_SEARCH_RESULT))

        searchViewModel.onItemClicked(0)
        searchViewModel.onLastItemChosen()

        searchViewModel
            .actions
            .test {
                skipItems(1)

                val action = awaitItem()
                assertThat(action).isEqualTo(
                    SearchAction.NavigateToDetail(TEST_IMAGE)
                )
            }
    }


    companion object {
        private val TEST_IMAGE = PixabayImage(1, "tags", "user", "url", "url", 1, 2, 3)

        private val IMAGE_SEARCH_RESULT = PixabayImageSearchResult(
            100,
            listOf(TEST_IMAGE)
        )
    }
}