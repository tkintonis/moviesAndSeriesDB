package com.example.moviesandseries.di.modules

import com.example.moviesandseries.common.helpers.DispatcherProvider
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class DispatchersModule {

    @Singleton
    @Provides
    fun providesDispatchersProvider() = DispatcherProvider()
}