package com.example.moviesandseries.searchScreen

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AbsListView
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.moviesandseries.activities.main.MainActivity
import com.example.moviesandseries.activities.main.MainViewModel
import com.example.moviesandseries.common.*
import com.example.moviesandseries.common.adapters.TvShowAdapter
import com.example.moviesandseries.common.models.TvShow
import com.example.moviesandseries.databinding.SearchFragmentLayoutBinding
import com.example.moviesandseries.di.components.FragmentSubComponent
import com.example.moviesandseries.network.UiState
import kotlinx.coroutines.Job
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

class SearchFragment : Fragment() {

    private lateinit var fragmentSubComponent: FragmentSubComponent
    @Inject
    lateinit var factory: SearchViewModel.Factory
    private lateinit var viewModel: SearchViewModel
    private val activityViewModel: MainViewModel by activityViewModels()

    private var binding: SearchFragmentLayoutBinding? = null
    private var searchResultsAdapter: TvShowAdapter? = null
    private var searchDelegationJob : Job? = null

    var isLoading = false
    var isLastPage = false
    var isScrolling = false

    private val scrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            super.onScrollStateChanged(recyclerView, newState)
            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                isScrolling = true
            }
        }

        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            val layoutManager = recyclerView.layoutManager as GridLayoutManager
            val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
            val visibleItemCount = layoutManager.childCount
            val totalItemCount = layoutManager.itemCount

            val isNotLoadingAndNotLastPage = !isLoading && !isLastPage
            val isAtLastItem = firstVisibleItemPosition + visibleItemCount >= totalItemCount
            val isNotAtBeginning = firstVisibleItemPosition >= 0
            val isTotalMoreThanVisible = totalItemCount >= viewModel.pageSize
            val shouldPaginate = isNotLoadingAndNotLastPage && isAtLastItem && isNotAtBeginning
                    && isTotalMoreThanVisible && isScrolling
            if (shouldPaginate) {
                viewModel.search(loadNextPage = true)
                isScrolling = false
            }
        }
    }

    private val queryTextListener = object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String?): Boolean {
            searchDelegationJob?.cancel()
            search(query)
            return true
        }

        override fun onQueryTextChange(newText: String?): Boolean {
            searchDelegationJob?.cancel()
            searchDelegationJob = MainScope().launch {
                delay(SEARCH_TYPE_DELAY)
                search(newText)
            }
            return true
        }
    }

    companion object {
        private const val SEARCH_TYPE_DELAY = 1000L
    }

    private fun search(query: String?) {
        if (query.isNullOrEmpty()) return
        viewModel.search(query)
        binding?.run { hideKeyboard(this.root) }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        fragmentSubComponent = (activity as MainActivity).activityDiComponent.fragmentComponent().create(this)
        fragmentSubComponent.inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this, factory)[SearchViewModel::class.java]
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = SearchFragmentLayoutBinding.inflate(layoutInflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupSearchView()
        setupSearchRecyclerView()
        setupFavButton()

        repeatWithLifecycle(Lifecycle.State.STARTED) {
            launch {
                viewModel.searchResult.collect { state ->
                    handleSearchResultUiState(state)
                }
            }
        }

        repeatWithLifecycle(Lifecycle.State.RESUMED) {
            launch {
                activityViewModel.favouriteShows.collect { favourites ->
                    handleListButton(favourites)
                }
            }
        }
    }

    private fun setupFavButton() {
        binding?.run {
            favListButton.setOnClickListener {
                val action = SearchFragmentDirections.actionSearchFragmentToFavouritesFragment()
                findNavController().safeNavigate(action)
            }
        }
    }

    private fun handleListButton(favourites: List<TvShow>) {
        binding?.run {
            if (favourites.isEmpty()) {
                favListButton.hide()
            }
            else {
                favListButton.show()
            }
        }
    }

    private fun setupSearchRecyclerView() {
        binding?.run {
            searchResultsAdapter = TvShowAdapter(resources) { tvShow -> navigateToDetails(tvShow) }
            with(searchRV) {
                layoutManager = GridLayoutManager(context, 2)
                adapter = searchResultsAdapter
                addOnScrollListener(this@SearchFragment.scrollListener)
            }
        }
    }

    private fun navigateToDetails(tvShow: TvShow) {
        binding?.searchView?.clearFocus()
        val action = SearchFragmentDirections.actionSearchFragmentToDetailsFragment(tvShow)
        findNavController().safeNavigate(action)
    }

    private fun handleSearchResultUiState(uiState: UiState<List<TvShow>>) {
        when (uiState) {
            is UiState.Loading -> {
                isLoading = true
                binding?.run {
                    popularMoviesProgressBar.show()
                }
            }
            is UiState.Success -> {
                isLoading = false
                binding?.run {
                    popularMoviesProgressBar.hide()
                    if (uiState.data.isNullOrEmpty()) return
                    searchResultsAdapter?.differ?.submitList(uiState.data.toList())
                    isLastPage = viewModel.pageNumber == viewModel.totalPages
                }
            }
            is UiState.Error -> {
                isLoading = false
                binding?.run {
                    popularMoviesProgressBar.hide()
                    showToastMessage(root, uiState.exception.message ?: "", resources) {
                        viewModel.search(retrying = true)
                    }
                }
            }
        }
    }

    private fun setupSearchView() {
        binding?.run {
            with(searchView) {
                setOnQueryTextListener(queryTextListener)
            }
        }
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }
}