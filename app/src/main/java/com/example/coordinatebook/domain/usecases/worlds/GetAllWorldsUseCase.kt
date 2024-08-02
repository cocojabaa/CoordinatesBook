package com.example.coordinatebook.domain.usecases.worlds

import com.example.coordinatebook.domain.models.WorldInfo

class GetAllWorldsUseCase {
    suspend fun execute(databaseApi: WorldsRepository): List<WorldInfo> {
        return databaseApi.getAllWorlds()
    }
}