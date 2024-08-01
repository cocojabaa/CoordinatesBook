package com.example.coordinatebook.domain

import com.example.coordinatebook.domain.models.WorldInfo

interface WorldsDatabaseApi {
    suspend fun getAllWorlds(): MutableList<WorldInfo>
    suspend fun deleteWorld(worldName: String): Boolean
    suspend fun addWorld(worldInfo: WorldInfo): Boolean
}