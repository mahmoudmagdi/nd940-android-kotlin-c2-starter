package com.udacity.asteroidradar.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.udacity.asteroidradar.database.AsteroidDataBaseDao

class MainViewModel(
    val database: AsteroidDataBaseDao,
    application: Application
) : AndroidViewModel(application) {


}