package com.udacity.asteroidradar.main

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.udacity.asteroidradar.Constants.API_KEY
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.Utils.getDate
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.api.parsePictureOfDayJsonResult
import com.udacity.asteroidradar.database.Asteroid
import com.udacity.asteroidradar.database.AsteroidDataBaseDao
import com.udacity.asteroidradar.api.AsteroidApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.lang.Exception

class MainViewModel(
    val database: AsteroidDataBaseDao,
    application: Application
) : AndroidViewModel(application) {

    private var viewModelJob = Job()

    private val _pictureOfDay = MutableLiveData<PictureOfDay?>()
    val pictureOfDay get() = _pictureOfDay

    val allAsteroids = database.getAllAsteroidsSortedByDate()

    private val _navigateToAsteroidDetails = MutableLiveData<Asteroid>()
    val navigateToAsteroidDetails
        get() = _navigateToAsteroidDetails

    init {
        getPictureOfDay()
        getAsteroidFromApi()
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    fun onAsteroidClicked(asteroid: Asteroid) {
        _navigateToAsteroidDetails.value = asteroid
    }

    fun onAsteroidDetailsNavigated() {
        _navigateToAsteroidDetails.value = null
    }

    private fun getAsteroidFromApi() {
        viewModelScope.launch {
            try {
                parseAsteroidsJsonResult(
                    JSONObject(
                        AsteroidApi.retrofitService.getNeatEarthObjects(
                            getDate(),
                            getDate(afterDays = 7),
                            API_KEY
                        )
                    )
                ).forEach {
                    Thread {
                        database.insert(it)
                    }.start()
                }
            } catch (e: Exception) {
                Log.e("TAG", "Failure: " + e.message)
                Toast.makeText(getApplication(), "Failure: " + e.message, Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun getPictureOfDay() {
        viewModelScope.launch {
            try {
                val result = AsteroidApi.retrofitService.getPictureOfDay(API_KEY)
                Log.e("result", result)
                _pictureOfDay.value = parsePictureOfDayJsonResult(JSONObject(result))
            } catch (e: Exception) {
                Log.e("TAG", "Failure: " + e.message)
                Toast.makeText(getApplication(), "Failure: " + e.message, Toast.LENGTH_LONG).show()
            }
        }
    }

}