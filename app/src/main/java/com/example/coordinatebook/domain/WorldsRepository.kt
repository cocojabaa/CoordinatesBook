package com.example.coordinatebook.domain

import com.example.coordinatebook.domain.models.WorldInfo

interface WorldsRepository {
    suspend fun getAllWorlds(): MutableList<WorldInfo>
    suspend fun deleteWorld(worldId: Int?): Boolean
    suspend fun addWorld(worldInfo: WorldInfo): Int?
    suspend fun getWorldIdByName(worldName: String): Int?
    suspend fun editWorld(newWorldInfo: WorldInfo): Boolean
}