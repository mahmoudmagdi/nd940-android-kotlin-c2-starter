package com.udacity.asteroidradar.database

import android.content.Context
import androidx.room.*
import com.udacity.asteroidradar.model.Asteroid

@Database(entities = [Asteroid::class], version = 1, exportSchema = false)
abstract class AsteroidDataBase : RoomDatabase() {

    abstract val asteroidDataBaseDao: AsteroidDataBaseDao

    companion object {

        @Volatile
        private var INSTANCE: AsteroidDataBase? = null

        fun getInstance(context: Context): AsteroidDataBase {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        AsteroidDataBase::class.java,
                        "asteroid_database"
                    ).fallbackToDestructiveMigration().build()
                    INSTANCE = instance
                }
                return instance
            }
        }
    }
}