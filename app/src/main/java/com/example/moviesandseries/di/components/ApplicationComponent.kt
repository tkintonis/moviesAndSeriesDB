package com.example.moviesandseries.di.components

import android.content.Context
import com.example.moviesandseries.di.common.ApplicationScope
import com.example.moviesandseries.di.modules.*
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@ApplicationScope
@Singleton
@Component(modules = [DispatchersModule::class, NetworkServiceModule::class, ActivitySubComponentModule::class, StorageModule::class, CommonModule::class])
interface ApplicationComponent {

    fun activityComponent(): ActivitySubcomponent.Factory

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): ApplicationComponent
    }
}