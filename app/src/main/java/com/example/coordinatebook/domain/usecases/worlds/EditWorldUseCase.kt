package com.example.coordinatebook.domain.usecases.worlds

import com.example.coordinatebook.domain.WorldsRepository
import com.example.coordinatebook.domain.models.WorldInfo

class EditWorldUseCase(val repository: WorldsRepository) {
    suspend fun execute(newWorldInfo: WorldInfo): Boolean {
        return repository.editWorld(newWorldInfo)
    }
}