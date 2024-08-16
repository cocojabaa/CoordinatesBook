package com.example.coordinatebook.data.coordinates

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface CoordinatesDao {
    @Query("SELECT * FROM coordinates WHERE worldId = :worldId")
    suspend fun getCoordinatesById(worldId: Int): MutableList<CoordinatesEntity>

    @Insert
    suspend fun addCoordinates(coordinatesEntity: CoordinatesEntity)

    @Query("DELETE FROM coordinates WHERE worldId = :worldId")
    suspend fun deleteAllCoordinatesById(worldId: Int)

    @Query("DELETE FROM coordinates WHERE id = :id")
    suspend fun deleteCoordinates(id: Int)

    @Query("SELECT * FROM coordinates " +
            "WHERE worldId = :worldId " +
            "AND description = :description " +
            "AND dimension = :dimension " +
            "AND x = :x " +
            "AND z = :z")
    suspend fun getCoordinatesId(worldId: Int?, description: String, dimension: String, x: Int, z: Int): CoordinatesEntity

    @Query("UPDATE coordinates " +
            "SET description = :description, " +
            "dimension = :dimension, " +
            "x = :x, " +
            "y = :y, " +
            "z = :z " +
            "WHERE id = :id")
    suspend fun editCoordinatesById(id: Int?, description: String, dimension: String, x: Int, y:Int?, z: Int)
}