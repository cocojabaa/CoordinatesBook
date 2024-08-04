package com.example.coordinatebook.data.coordinates

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface CoordinatesDao {
    @Query("SELECT * FROM coordinates WHERE id = :id")
    fun getCoordinatesById(id: Int): MutableList<CoordinatesEntity>

    @Insert
    fun addCoordinates(coordinatesEntity: CoordinatesEntity)
}