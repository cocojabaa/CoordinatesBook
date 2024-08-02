package com.example.coordinatebook.domain.usecases.worlds

class DeleteWorldUseCase {
    suspend fun execute(worldName: String, databaseApi: WorldsRepository): Boolean {
        return databaseApi.deleteWorld(worldName)
    }
}