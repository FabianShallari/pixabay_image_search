package com.fabianshallari.pixabay.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.fabianshallari.pixabay.confirmation.ConfirmationFragment
import com.fabianshallari.pixabay.databinding.SearchViewBinding
import com.fabianshallari.pixabay.databinding.SearchViewBinding.inflate
import com.fabianshallari.pixabay.extensions.requireAdapter
import com.fabianshallari.pixabay.extensions.textChanges
import com.fabianshallari.pixabay.extensions.viewBinding
import com.fabianshallari.pixabay.navigation.Routes
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@OptIn(FlowPreview::class)
@AndroidEntryPoint
class SearchFragment : Fragment() {

    private val searchViewModel: SearchViewModel by viewModels()
    private val searchView: SearchViewBinding by viewBinding(::inflate)
    private val itemClickListener: ItemClickListener = object : ItemClickListener {
        override fun onItemClicked(position: Int) {
            searchViewModel.onItemClicked(position)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = searchView.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                with(searchView) {
                    swipeRefresh.setOnRefreshListener {
                        searchViewModel.search()
                    }

                    setFragmentResultListener(ConfirmationFragment.CONFIRMATION_REQUEST) { _, bundle ->
                        if (bundle.getBoolean(ConfirmationFragment.CONFIRMATION_RESPONSE)) {
                            searchViewModel.onLastItemChosen()
                        }
                    }

                    imageResultsGrid.layoutManager = GridLayoutManager(requireContext(), 2)
                    imageResultsGrid.adapter = ImageResultsAdapter(itemClickListener).apply {
                        setHasStableIds(true)
                    }

                    searchTerm
                        .textChanges()
                        .filter {
                            it != searchViewModel.state.value.searchTerm
                        }
                        .debounce(600)
                        .onEach {
                            searchViewModel.search(it)
                        }.launchIn(this@repeatOnLifecycle)

                    searchViewModel
                        .state
                        .onEach { searchState ->
                            with(searchView) {
                                swipeRefresh.isRefreshing = searchState.isLoading
                                searchTerm.setTextKeepState(searchState.searchTerm)
                                requireAdapter<ImageResultsAdapter>(imageResultsGrid).update(
                                    searchState.images
                                )
                            }
                        }.launchIn(this@repeatOnLifecycle)

                    searchViewModel
                        .actions
                        .onEach { searchAction ->
                            when (searchAction) {
                                SearchAction.NavigateToConfirmation -> {
                                    findNavController().navigate(Routes.Confirmation)
                                }
                                is SearchAction.NavigateToDetail -> {
                                    findNavController()
                                        .navigate(Routes.ImageDetail.routeWithId(searchAction.pixabayImage.id))
                                }
                                is SearchAction.ShowErrorMessage -> {
                                    Toast
                                        .makeText(
                                            root.context,
                                            searchAction.message,
                                            Toast.LENGTH_LONG
                                        )
                                        .show()
                                }
                            }
                        }.launchIn(this@repeatOnLifecycle)
                }
            }
        }
    }


}