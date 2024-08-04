package com.example.coordinatebook.domain.usecases.worlds

import com.example.coordinatebook.domain.WorldsRepository
import com.example.coordinatebook.domain.models.WorldInfo

class AddWorldUseCase(val repository: WorldsRepository) {
    suspend fun execute(worldInfo: WorldInfo): Int? {
        return repository.addWorld(worldInfo)
    }
}