package com.example.moviesandseries.db

import androidx.room.*
import com.example.moviesandseries.common.models.TvShow

@Dao
interface FavouriteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(tvShow: TvShow): Long

    @Query("SELECT * FROM favourites")
    suspend fun getAllFavourites() : List<TvShow>

    @Query("SELECT * FROM favourites WHERE title LIKE '%' || :query || '%'")
    suspend fun searchFavourite(query: String) : List<TvShow>

    @Query("SELECT EXISTS(SELECT * FROM favourites WHERE id = :showId)")
    suspend fun isInFavourites(showId: Int) : Boolean

    @Delete
    suspend fun deleteFavourite(tvShow: TvShow)
}