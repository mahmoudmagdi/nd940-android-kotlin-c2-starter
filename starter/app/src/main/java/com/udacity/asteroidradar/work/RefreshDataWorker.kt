package com.udacity.asteroidradar.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.udacity.asteroidradar.database.AsteroidDataBase
import com.udacity.asteroidradar.repository.AsteroidsRepo
import com.udacity.asteroidradar.utils.getDate
import retrofit2.HttpException

class RefreshDataWorker(appContext: Context, params: WorkerParameters) :
    CoroutineWorker(appContext, params) {

    companion object {
        const val WORK_NAME = "RefreshDataWorker"
    }

    override suspend fun doWork(): Result {
        val database = AsteroidDataBase.getInstance(applicationContext)
        val repository = AsteroidsRepo(database)
        return try {
            repository.refreshData(getDate(), getDate(7))
            Result.success()
        } catch (e: HttpException) {
            Result.retry()
        }
    }
}