package com.example.moviesandseries.di.modules

import android.content.Context
import androidx.room.Room
import com.example.moviesandseries.db.FavouritesDataBase
import com.example.moviesandseries.di.common.ApplicationScope
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class StorageModule {

    @Singleton
    @Provides
    fun providesDataBase(@ApplicationScope context: Context) : FavouritesDataBase {
        return Room.databaseBuilder(
            context,
            FavouritesDataBase::class.java,
            "tvshows_db.db"
        ).build()
    }
}