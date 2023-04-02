package com.example.moviesandseries.searchScreen

import android.content.res.Resources
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.moviesandseries.R
import com.example.moviesandseries.common.enums.ShowType
import com.example.moviesandseries.common.dto.BasePaginatedResponse
import com.example.moviesandseries.common.dto.TvShowDto
import com.example.moviesandseries.common.models.TvShow
import com.example.moviesandseries.network.Resource
import com.example.moviesandseries.network.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

class SearchViewModel(
    private val repository: SearchRepository,
    private val resources: Resources
) : ViewModel() {

    var pageNumber: Int = 0
    var totalPages: Int = 0
    var pageSize: Int = 0
    private var searchQuery: String = ""

    private val searchList = mutableListOf<TvShow>()
    private val _searchResult = MutableStateFlow<UiState<MutableList<TvShow>>>(UiState.Success(null))
    val searchResult = _searchResult.asStateFlow()

    fun search(newQuery: String = searchQuery, loadNextPage: Boolean = false, retrying: Boolean = false) {

        if (newQuery == searchQuery && !loadNextPage && !retrying) return
        if (!loadNextPage && !retrying) resetSearchAndPagination()

        pageNumber++
        searchQuery = newQuery
        requestSearch()
    }

    private fun resetSearchAndPagination() {
        pageNumber = 0
        totalPages = 0
        pageSize = 0
        searchList.clear()
    }

    private fun requestSearch() {
        viewModelScope.launch {
            _searchResult.value = UiState.Loading()
            val response = repository.searchTvShow(searchQuery, pageNumber)
            handleSearchResponse(response)
        }
    }

    private fun handleSearchResponse(response: Resource<BasePaginatedResponse<TvShowDto>>) {
        when (response) {
            is Resource.Success -> {
                response.data?.let { result ->
                    handlePaginationData(result.pageNumber, result.totalPages)
                    handleShowList(result)
                }
            }
            is Resource.Error -> {
                pageNumber--
                _searchResult.value = UiState.Error(response.exception)
            }
        }
    }

    private fun handlePaginationData(pageNumber: Int, totalPages: Int) {
        this.pageNumber = pageNumber
        this.totalPages = totalPages
    }

    private fun handleShowList(result: BasePaginatedResponse<TvShowDto>) {
        if (result.tvShowsList.isEmpty()) {
            _searchResult.value = UiState.Error(Exception(resources.getString(R.string.nothing_found)))
            return
        }
        val newList = result.tvShowsList.map { TvShow(it) }.filter {
            it.mediaType == ShowType.SERIES || it.mediaType == ShowType.MOVIE
        }
        searchList.addAll(newList)
        pageSize = newList.size
        _searchResult.value = UiState.Success(searchList)
    }

    class Factory @Inject constructor(
        private val repository: SearchRepository,
        private val resources: Resources
    ) : ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(SearchViewModel::class.java)) {
                return SearchViewModel(
                    repository, resources
                ) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }

}