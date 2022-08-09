package com.udacity.asteroidradar.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface AsteroidDataBaseDao {

    @Insert
    fun insert(asteroid: Asteroid)

    @Query("SELECT * FROM asteroid_table ORDER BY id DESC")
    fun getAllAsteroids(): LiveData<List<Asteroid>>

    @Update
    suspend fun update(asteroid: Asteroid)

    @Query("SELECT * FROM asteroid_table WHERE id = :key")
    fun get(key: Long): Asteroid?

    @Query("DELETE FROM asteroid_table")
    suspend fun clear()
}