package com.example.moviesandseries.activities.main

import com.example.moviesandseries.common.models.TvShow
import com.example.moviesandseries.db.FavouritesDataBase
import javax.inject.Inject


class MainRepository @Inject constructor(private val db: FavouritesDataBase) : MainRepositoryIf {

    override suspend fun getFavourites(): List<TvShow> {
        return db.getFavouritesDao().getAllFavourites()
    }

    override suspend fun searchInFavourites(query: String): List<TvShow> {
        return db.getFavouritesDao().searchFavourite(query)
    }

    override suspend fun belongsToFavourites(tvShowId: Int): Boolean {
        return db.getFavouritesDao().isInFavourites(tvShowId)
    }

    override suspend fun addToFavourites(tvShow: TvShow) {
        db.getFavouritesDao().insert(tvShow)
    }

    override suspend fun deleteFromFavourites(tvShow: TvShow) {
        db.getFavouritesDao().deleteFavourite(tvShow)
    }
}