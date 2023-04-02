package com.example.moviesandseries.network

import com.example.moviesandseries.common.constants.Constants.API_KEY
import com.example.moviesandseries.common.constants.Constants.SEARCH_URL
import com.example.moviesandseries.common.dto.BasePaginatedResponse
import com.example.moviesandseries.common.dto.TvShowDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NetworkServiceApi {

    @GET(SEARCH_URL)
    suspend fun searchTvShow(
        @Query("api_key") apiKey: String = API_KEY,
        @Query("query") searchQuery: String,
        @Query("page") pageNumber: Int
    ) : Response<BasePaginatedResponse<TvShowDto>>
}