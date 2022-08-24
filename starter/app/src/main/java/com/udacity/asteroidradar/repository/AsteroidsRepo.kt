package com.udacity.asteroidradar.repository

import com.udacity.asteroidradar.model.PictureOfDay
import com.udacity.asteroidradar.api.AsteroidApi
import com.udacity.asteroidradar.utils.parseAsteroidsJsonResult
import com.udacity.asteroidradar.database.AsteroidDataBase
import com.udacity.asteroidradar.utils.asDatabaseModel
import com.udacity.asteroidradar.utils.getDate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject

class AsteroidsRepo(private val database: AsteroidDataBase) {

    suspend fun refreshData(
        startDate: String = getDate(),
        endDate: String = getDate(),
    ) {
        withContext(Dispatchers.IO) {
            val response =
                AsteroidApi.retrofitService.getNeatEarthObjectsAsync(startDate, endDate).await()
            val asteroidsList = parseAsteroidsJsonResult(JSONObject(response.string()))
            database.asteroidDataBaseDao.insertAll(*asteroidsList.asDatabaseModel())
        }
    }

    suspend fun loadPictureOfDay(): PictureOfDay? {
        var pictureOfDay: PictureOfDay
        withContext(Dispatchers.IO) {
            pictureOfDay = AsteroidApi.retrofitService.getPictureOfDayAsync().await()
        }
        return if (pictureOfDay.mediaType == "image") {
            pictureOfDay
        } else {
            null
        }
    }

    suspend fun deleteOldData() {
        withContext(Dispatchers.IO) {
            database.asteroidDataBaseDao.deleteOldAsteroids(getDate())
        }
    }
}