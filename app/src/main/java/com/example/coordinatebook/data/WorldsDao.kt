package com.example.coordinatebook.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface WorldsDao {

    @Query("SELECT * FROM worlds")
    fun getAllWorlds(): List<WorldEntity>

    @Insert
    fun addWorld(world: WorldEntity)

    @Delete
    fun deleteWorld(world: WorldEntity)

}