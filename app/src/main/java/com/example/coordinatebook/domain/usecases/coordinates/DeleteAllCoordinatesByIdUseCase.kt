package com.example.coordinatebook.domain.usecases.coordinates

import com.example.coordinatebook.domain.CoordinatesRepository

class DeleteAllCoordinatesByIdUseCase(val repository: CoordinatesRepository) {
    suspend fun execute(worldId: Int): Boolean {
        return repository.deleteAllCoordinatesByWorldId(worldId)
    }
}