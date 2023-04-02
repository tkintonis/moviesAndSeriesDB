package com.example.moviesandseries.di.modules

import android.content.Context
import android.content.res.Resources
import com.example.moviesandseries.di.common.ApplicationScope
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class CommonModule {

    @Singleton
    @Provides
    fun getSystemResources(@ApplicationScope context: Context) : Resources {
        return context.resources
    }
}