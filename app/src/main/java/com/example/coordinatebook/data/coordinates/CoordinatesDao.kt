package com.example.coordinatebook.data.coordinates

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface CoordinatesDao {
    @Query("SELECT * FROM coordinates WHERE worldId = :worldId")
    suspend fun getCoordinatesById(worldId: Int): MutableList<CoordinatesEntity>

    @Insert
    suspend fun addCoordinates(coordinatesEntity: CoordinatesEntity)

    @Query("DELETE FROM coordinates WHERE worldId = :worldId")
    suspend fun deleteAllCoordinatesById(worldId: Int)

    @Delete
    suspend fun deleteCoordinates(coordinatesEntity: CoordinatesEntity)
}