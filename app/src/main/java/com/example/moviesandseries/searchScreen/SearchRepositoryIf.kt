package com.example.moviesandseries.searchScreen

import com.example.moviesandseries.common.dto.BasePaginatedResponse
import com.example.moviesandseries.common.dto.TvShowDto
import com.example.moviesandseries.network.Resource

interface SearchRepositoryIf {

    suspend fun searchTvShow(searchQuery: String, pageNumber: Int): Resource<BasePaginatedResponse<TvShowDto>>

}
