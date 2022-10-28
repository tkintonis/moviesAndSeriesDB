package com.example.moviesandseries

import android.app.Application
import com.example.moviesandseries.di.components.ApplicationComponent
import com.example.moviesandseries.di.components.DaggerApplicationComponent

class App : Application() {

    lateinit var applicationComponent: ApplicationComponent
        private set

    override fun onCreate() {
        super.onCreate()

        applicationComponent = DaggerApplicationComponent.factory().create(this)
    }
}