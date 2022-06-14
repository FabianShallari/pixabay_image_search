package com.fabianshallari.pixabay.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.*
import coil.load
import com.fabianshallari.pixabay.R
import com.fabianshallari.pixabay.databinding.ImageDetailsBinding
import com.fabianshallari.pixabay.databinding.ImageDetailsBinding.inflate
import com.fabianshallari.pixabay.extensions.viewBinding
import com.fabianshallari.pixabay.navigation.Arguments
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ImageDetailFragment : Fragment() {

    private val viewBinding: ImageDetailsBinding by viewBinding(::inflate)

    @Inject
    lateinit var viewModelFactory: ImageDetailViewModel.Factory

    private val viewModel: ImageDetailViewModel by viewModels(factoryProducer = {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T =
                viewModelFactory.create(imageId) as T
        }
    })

    private val imageId: Long
        get() = requireArguments().getLong(Arguments.PixabayImage)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = viewBinding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewBinding.swipeRefresh.setOnRefreshListener { viewModel.search() }

        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel
                    .actions
                    .onEach { action ->
                        when (action) {
                            is ImageDetailAction.ShowErrorMessage -> {
                                Toast
                                    .makeText(requireContext(), action.message, Toast.LENGTH_LONG)
                                    .show()
                            }
                        }
                    }
                    .launchIn(this@repeatOnLifecycle)


                viewModel
                    .state
                    .onEach { state ->
                        with(viewBinding) {
                            swipeRefresh.isRefreshing = state.isLoading
                            imageContainer.isVisible = state.pixabayImage != null
                            state.pixabayImage?.let { pixabayImage ->
                                image.load(pixabayImage.largeImageURL)
                                downloads.text = getString(R.string.downloads, pixabayImage.downloads.toString())
                                userName.text = getString(R.string.uploaded_by, pixabayImage.user)
                                likes.text = getString(R.string.likes, pixabayImage.likes.toString())
                                comments.text = getString(R.string.comments, pixabayImage.comments.toString())
                                tags.text = pixabayImage.tags
                            }
                        }
                    }
                    .launchIn(this@repeatOnLifecycle)
            }
        }

    }

}