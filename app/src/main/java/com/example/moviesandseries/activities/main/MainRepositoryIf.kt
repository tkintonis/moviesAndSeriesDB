package com.example.moviesandseries.activities.main

import com.example.moviesandseries.common.models.TvShow

interface MainRepositoryIf {

    suspend fun getFavourites() : List<TvShow>
    suspend fun searchInFavourites(query: String) : List<TvShow>
    suspend fun belongsToFavourites(tvShowId: Int): Boolean
    suspend fun addToFavourites(tvShow: TvShow)
    suspend fun deleteFromFavourites(tvShow: TvShow)
}