package com.example.moviesandseries.activities.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.moviesandseries.common.models.TvShow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class MainViewModel(private val repository: MainRepository) : ViewModel() {

    private val _belongsToFavourites = MutableStateFlow(false)
    val belongsToFavourites = _belongsToFavourites.asStateFlow()

    private val _favouriteShows = MutableStateFlow<List<TvShow>>(emptyList())
    val favouriteShows = _favouriteShows.asStateFlow()

    private var searchQuery = ""

    init {
        getAllFavourites(searchQuery)
    }

    fun getAllFavourites(query: String = "") {
        searchQuery = query
        if (searchQuery.isEmpty()) {
            viewModelScope.launch {
                val result = repository.getFavourites()
                _favouriteShows.value = result
            }
        } else {
            viewModelScope.launch {
                val result = repository.searchInFavourites(searchQuery)
                _favouriteShows.value = result
            }
        }
    }

    fun searchIfShowIsFavourite(id: Int) {
        viewModelScope.launch {
            val result = repository.belongsToFavourites(id)
            _belongsToFavourites.value = result
        }
    }

    fun addOrRemoveFromFavourites(tvShow: TvShow) {
        viewModelScope.launch {
            if (tvShow.isFavourite) {
                repository.deleteFromFavourites(tvShow)
            } else {
                repository.addToFavourites(tvShow)
            }
            getAllFavourites(searchQuery)
        }
    }

    class Factory @Inject constructor(
        private val repository: MainRepository
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
                return MainViewModel(
                    repository
                ) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}