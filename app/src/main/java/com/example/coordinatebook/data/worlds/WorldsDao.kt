package com.example.coordinatebook.data.worlds

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface WorldsDao {

    @Query("SELECT * FROM worlds")
    suspend fun getAllWorlds(): List<WorldEntity>

    @Insert
    suspend fun addWorld(world: WorldEntity)

    @Query("DELETE FROM worlds WHERE id = :worldId")
    suspend fun deleteWorld(worldId: Int?)

    @Query("SELECT * FROM worlds WHERE name = :worldName")
    suspend fun getWorldByName(worldName: String): WorldEntity

    @Query("UPDATE worlds SET name = :name, description = :description WHERE id = :worldId")
    suspend fun editWorld(worldId: Int?, name: String, description: String)
}