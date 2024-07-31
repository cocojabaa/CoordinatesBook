package com.example.coordinatebook.domain

import com.example.coordinatebook.domain.models.WorldInfo

interface WorldsDatabaseApi {
    fun getAllWorlds(): MutableList<WorldInfo>
    fun deleteWorld(worldInfo: WorldInfo)
    fun addWorld(worldInfo: WorldInfo)
}