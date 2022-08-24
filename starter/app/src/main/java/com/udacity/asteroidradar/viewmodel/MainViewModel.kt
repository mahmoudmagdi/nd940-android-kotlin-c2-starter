package com.udacity.asteroidradar.viewmodel

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.udacity.asteroidradar.model.PictureOfDay
import com.udacity.asteroidradar.database.AsteroidDataBase
import com.udacity.asteroidradar.model.Asteroid
import com.udacity.asteroidradar.repository.AsteroidsRepo
import com.udacity.asteroidradar.utils.getDate
import kotlinx.coroutines.launch

class MainViewModel(
    val database: AsteroidDataBase,
    application: Application
) : AndroidViewModel(application) {

    private val asteroidRepo = AsteroidsRepo(database)

    private val _pictureOfDay = MutableLiveData<PictureOfDay?>()
    val pictureOfDay: LiveData<PictureOfDay?>
        get() = _pictureOfDay

    private val _navigateToAsteroidDetails = MutableLiveData<Asteroid>()
    val navigateToAsteroidDetails: LiveData<Asteroid>
        get() = _navigateToAsteroidDetails

    private val _allAsteroids = MutableLiveData<List<Asteroid>?>()
    val allAsteroids: LiveData<List<Asteroid>?>
        get() = _allAsteroids

    init {
        viewModelScope.launch {
            try {
                asteroidRepo.refreshData(getDate(), getDate(7))
                _pictureOfDay.value = asteroidRepo.loadPictureOfDay()
            } catch (e: Exception) {
                Log.e("MainViewModel", e.message.toString())
                Toast.makeText(application, "Failure: ${e.message}", Toast.LENGTH_LONG).show()
            }
        }
        getTodayAsteroids()
    }

    fun onAsteroidClicked(asteroid: Asteroid) {
        _navigateToAsteroidDetails.value = asteroid
    }

    fun onAsteroidDetailsNavigated() {
        _navigateToAsteroidDetails.value = null
    }

    fun getWeekAsteroids() {
        viewModelScope.launch {
            database.asteroidDataBaseDao.getAllAsteroidsSortedByDate(getDate(), getDate(7))
                .collect {
                    _allAsteroids.value = it
                }
        }
    }

    fun getTodayAsteroids() {
        viewModelScope.launch {
            database.asteroidDataBaseDao.getAllAsteroidsSortedByDate(getDate(), getDate()).collect {
                _allAsteroids.value = it
            }
        }
    }

    fun getSavedAsteroids() {
        viewModelScope.launch {
            database.asteroidDataBaseDao.getAllAsteroids().collect {
                _allAsteroids.value = it
            }
        }
    }
}