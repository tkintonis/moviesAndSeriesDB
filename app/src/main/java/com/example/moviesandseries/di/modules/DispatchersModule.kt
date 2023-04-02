package com.example.moviesandseries.di.modules

import com.example.moviesandseries.common.helpers.DispatcherProvider
import dagger.Module
import dagger.Provides

@Module
class DispatchersModule {

    @Provides
    fun providesDispatchersProvider() = DispatcherProvider()
}