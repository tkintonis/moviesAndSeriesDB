package com.example.moviesandseries.activities.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.moviesandseries.App
import com.example.moviesandseries.databinding.ActivityMainBinding
import com.example.moviesandseries.di.components.ActivitySubcomponent
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var factory: MainViewModel.Factory
    private lateinit var viewModel: MainViewModel

    lateinit var activityDiComponent: ActivitySubcomponent
        private set

    private var binding: ActivityMainBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        activityDiComponent = (application as App).applicationComponent.activityComponent().create(this)
        activityDiComponent.inject(this)

        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this, factory)[MainViewModel::class.java]
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)
    }

    override fun onDestroy() {
        binding = null
        super.onDestroy()
    }
}