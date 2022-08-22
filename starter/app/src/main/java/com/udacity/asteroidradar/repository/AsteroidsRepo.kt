package com.udacity.asteroidradar.repository

import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.Utils.getDate
import com.udacity.asteroidradar.api.AsteroidApi
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.api.parsePictureOfDayJsonResult
import com.udacity.asteroidradar.database.AsteroidDataBase
import com.udacity.asteroidradar.database.asDatabaseModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject

class AsteroidsRepo(private val database: AsteroidDataBase) {

    suspend fun refreshData(
        startDate: String = getDate(),
        endDate: String = getDate(7),
    ) {
        withContext(Dispatchers.IO) {
            val response =
                AsteroidApi.retrofitService.getNeatEarthObjects(startDate, endDate)
            val asteroidsList = parseAsteroidsJsonResult(JSONObject(response))
            database.asteroidDataBaseDao.insertAll(*asteroidsList.asDatabaseModel())
        }
    }

    suspend fun loadPictureOfDay(): PictureOfDay {
        var pictureOfDay: PictureOfDay
        withContext(Dispatchers.IO) {
            val pictureOfDayResponse = AsteroidApi.retrofitService.getPictureOfDay()
            pictureOfDay = parsePictureOfDayJsonResult(JSONObject(pictureOfDayResponse))
        }
        return pictureOfDay
    }
}