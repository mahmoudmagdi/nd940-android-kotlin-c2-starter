package com.udacity.asteroidradar.api

import com.udacity.asteroidradar.utils.Constants.API_KEY
import com.udacity.asteroidradar.model.PictureOfDay
import kotlinx.coroutines.Deferred
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Query

interface AsteroidApiService {

    @GET("neo/rest/v1/feed")
    fun getNeatEarthObjectsAsync(
        @Query("start_date") startDate: String,
        @Query("end_date") endDate: String,
        @Query("api_key") apiKey: String = API_KEY
    ): Deferred<ResponseBody>

    @GET("planetary/apod")
    fun getPictureOfDayAsync(
        @Query("api_key") apiKey: String = API_KEY
    ): Deferred<PictureOfDay>
}