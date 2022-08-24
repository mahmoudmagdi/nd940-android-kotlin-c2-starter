package com.udacity.asteroidradar.utils

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.udacity.asteroidradar.database.AsteroidDataBase
import com.udacity.asteroidradar.viewmodel.MainViewModel

class MainViewModelFactory(
    private val dataSource: AsteroidDataBase,
    private val application: Application
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(dataSource, application) as T
        }
        throw IllegalArgumentException("Unknown viewModel class")
    }
}