package com.example.coordinatebook.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface WorldsDao {

    @Query("SELECT * FROM worlds")
    suspend fun getAllWorlds(): List<WorldEntity>

    @Insert
    suspend fun addWorld(world: WorldEntity)

    @Query("DELETE FROM worlds WHERE Name = :name")
    suspend fun deleteWorld(name: String)

}