package com.example.moviesandseries.di.components

import androidx.fragment.app.Fragment
import com.example.moviesandseries.detailsScreen.DetailsFragment
import com.example.moviesandseries.di.common.FragmentScope
import com.example.moviesandseries.favouritesScreen.FavouritesFragment
import com.example.moviesandseries.searchScreen.SearchFragment
import dagger.BindsInstance
import dagger.Subcomponent

@FragmentScope
@Subcomponent(modules = [])
interface FragmentSubComponent {

    fun inject(fragment: SearchFragment)
    fun inject(fragment: DetailsFragment)
    fun inject(fragment: FavouritesFragment)

    @Subcomponent.Factory
    interface Factory {
        fun create(@BindsInstance fragment: Fragment): FragmentSubComponent
    }
}