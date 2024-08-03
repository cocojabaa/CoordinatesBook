package com.example.coordinatebook.domain.usecases.worlds

import com.example.coordinatebook.domain.WorldsRepository
import com.example.coordinatebook.domain.models.WorldInfo

class GetAllWorldsUseCase(val repository: WorldsRepository) {
    suspend fun execute(): MutableList<WorldInfo> {
        return repository.getAllWorlds()
    }
}