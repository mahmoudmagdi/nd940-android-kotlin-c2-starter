package com.udacity.asteroidradar.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface AsteroidDataBaseDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(asteroid: Asteroid)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(vararg manyAsteroids: Asteroid)

    @Query("SELECT * FROM asteroids_table WHERE closeApproachDate >= :startDate AND closeApproachDate <= :endDate ORDER BY closeApproachDate ASC")
    fun getAllAsteroids(startDate: String, endDate: String): LiveData<List<Asteroid>>

    @Query("SELECT * FROM asteroids_table ORDER BY closeApproachDate ASC")
    fun getAllAsteroidsSortedByDate(): LiveData<List<Asteroid>>

//    @Query("DELETE FROM asteroids_table WHERE closeApproachDate < :date ORDER BY closeApproachDate ASC")
//    fun deleteAllAsteroidsBeforeToday(date: String)

    @Update
    suspend fun update(asteroid: Asteroid)

    @Query("SELECT * FROM asteroids_table WHERE id = :key")
    fun get(key: Long): Asteroid?

    @Query("DELETE FROM asteroids_table")
    suspend fun clear()
}