package com.example.moviesandseries.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.moviesandseries.common.models.TvShow
import javax.inject.Singleton

@Singleton
@Database(
    entities = [TvShow::class],
    version = 1
)
abstract class FavouritesDataBase : RoomDatabase() {

    abstract fun getFavouritesDao() : FavouriteDao
}