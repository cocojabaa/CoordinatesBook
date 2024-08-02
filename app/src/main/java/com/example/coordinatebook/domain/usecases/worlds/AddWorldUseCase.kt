package com.example.coordinatebook.domain.usecases.worlds

import com.example.coordinatebook.domain.models.WorldInfo

class AddWorldUseCase {
    suspend fun execute(worldInfo: WorldInfo, databaseApi: WorldsRepository): Boolean {
        return databaseApi.addWorld(worldInfo)
    }
}