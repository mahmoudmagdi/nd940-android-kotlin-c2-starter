package com.udacity.asteroidradar.database

import androidx.room.*
import com.udacity.asteroidradar.model.Asteroid
import kotlinx.coroutines.flow.Flow

@Dao
interface AsteroidDataBaseDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg manyAsteroids: Asteroid)

    @Query("SELECT * FROM asteroids_table ORDER BY closeApproachDate ASC")
    fun getAllAsteroids(): Flow<List<Asteroid>>

    @Query("SELECT * FROM asteroids_table WHERE closeApproachDate >= :startDate AND closeApproachDate <= :endDate ORDER BY closeApproachDate ASC")
    fun getAllAsteroidsSortedByDate(startDate: String, endDate: String): Flow<List<Asteroid>>

    @Query("DELETE FROM asteroids_table WHERE closeApproachDate < :date")
    fun deleteOldAsteroids(date: String)
}