package com.example.coordinatebook.domain.usecases

import com.example.coordinatebook.domain.WorldsDatabaseApi
import com.example.coordinatebook.domain.models.WorldInfo

class DeleteWorldUseCase {
    suspend fun execute(worldInfo: WorldInfo, databaseApi: WorldsDatabaseApi): Boolean {
        return databaseApi.deleteWorld(worldInfo)
    }
}