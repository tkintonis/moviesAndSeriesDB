package com.example.moviesandseries.di.components

import android.content.Context
import com.example.moviesandseries.activities.main.MainActivity
import com.example.moviesandseries.di.common.ActivityScope
import com.example.moviesandseries.di.modules.FragmentSubComponentModule
import dagger.BindsInstance
import dagger.Subcomponent

@ActivityScope
@Subcomponent(modules = [FragmentSubComponentModule::class])
interface ActivitySubcomponent {

    fun fragmentComponent(): FragmentSubComponent.Factory
    fun inject(activity: MainActivity)

    @Subcomponent.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): ActivitySubcomponent
    }

}