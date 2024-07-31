package com.example.coordinatebook.domain.usecases

import com.example.coordinatebook.domain.WorldsDatabaseApi
import com.example.coordinatebook.domain.models.WorldInfo

class GetAllWorldsUseCase {
    fun execute(databaseApi: WorldsDatabaseApi): List<WorldInfo> {
        return databaseApi.getAllWorlds()
    }
}