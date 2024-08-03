package com.example.coordinatebook.domain.usecases.worlds

import com.example.coordinatebook.domain.WorldsRepository

class DeleteWorldUseCase(val repository: WorldsRepository) {
    suspend fun execute(worldName: String): Boolean {
        return repository.deleteWorld(worldName)
    }
}