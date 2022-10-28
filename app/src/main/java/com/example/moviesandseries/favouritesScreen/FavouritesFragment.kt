package com.example.moviesandseries.favouritesScreen

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.moviesandseries.activities.main.MainActivity
import com.example.moviesandseries.activities.main.MainViewModel
import com.example.moviesandseries.common.*
import com.example.moviesandseries.common.adapters.TvShowAdapter
import com.example.moviesandseries.common.models.TvShow
import com.example.moviesandseries.databinding.FavouritesFragmentLayoutBinding
import com.example.moviesandseries.di.components.FragmentSubComponent
import kotlinx.coroutines.launch

class FavouritesFragment : Fragment() {

    private lateinit var fragmentSubComponent: FragmentSubComponent
    private val viewModel: MainViewModel by activityViewModels()
    private var binding: FavouritesFragmentLayoutBinding? = null

    private var favouritesAdapter: TvShowAdapter? = null
    private val queryTextListener = object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String?): Boolean {
            search(query, true)
            return true
        }

        override fun onQueryTextChange(newText: String?): Boolean {
            search(newText)
            return true
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        fragmentSubComponent = (activity as MainActivity).activityDiComponent.fragmentComponent().create(this)
        fragmentSubComponent.inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FavouritesFragmentLayoutBinding.inflate(inflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar()
        setupSearchView()
        setupRecyclerView()
        repeatWithLifecycle(Lifecycle.State.STARTED) {
            launch {
                viewModel.favouriteShows.collect {
                    favouritesAdapter?.differ?.submitList(it)
                }
            }
        }
    }

    private fun search(query: String?, shouldHideKeyboard: Boolean = false) {
        if (query.isNullOrEmpty()) {
            viewModel.getAllFavourites()
            return
        }
        viewModel.getAllFavourites(query)
        if(shouldHideKeyboard) binding?.run { hideKeyboard(this.root) }
    }

    private fun setupToolbar() {
        binding?.run {
            toolbar.setupWithNavController(findNavController())
            searchBtn.setOnClickListener {
                if (searchView.isVisible) {
                    searchView.hide()
                } else {
                    searchView.show()
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

    private fun setupRecyclerView() {
        binding?.run {
            favouritesAdapter = TvShowAdapter(resources) { tvShow ->
                navigateToDetails(tvShow)
            }
            with(favouritesRV) {
                layoutManager = GridLayoutManager(context, 3)
                adapter = favouritesAdapter
            }
        }
    }

    private fun navigateToDetails(it: TvShow) {
        binding?.searchView?.clearFocus()
        val action = FavouritesFragmentDirections.actionFavouritesFragmentToDetailsFragment(it)
        findNavController().safeNavigate(action)
    }

    override fun onDestroyView() {
        viewModel.getAllFavourites()
        binding = null
        super.onDestroyView()
    }
}