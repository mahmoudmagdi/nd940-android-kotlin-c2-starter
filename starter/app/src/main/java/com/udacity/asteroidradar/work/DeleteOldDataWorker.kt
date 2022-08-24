package com.udacity.asteroidradar.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.udacity.asteroidradar.database.AsteroidDataBase
import com.udacity.asteroidradar.repository.AsteroidsRepo
import retrofit2.HttpException

class DeleteOldDataWorker(appContext: Context, params: WorkerParameters) :
    CoroutineWorker(appContext, params) {

    companion object {
        const val WORK_NAME = "DeleteOldDataWorker"
    }

    override suspend fun doWork(): Result {
        val database = AsteroidDataBase.getInstance(applicationContext)
        val repository = AsteroidsRepo(database)
        return try {
            repository.deleteOldData()
            Result.success()
        } catch (e: HttpException) {
            Result.retry()
        }
    }
}