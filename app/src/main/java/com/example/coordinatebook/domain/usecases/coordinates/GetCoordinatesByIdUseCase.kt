package com.example.coordinatebook.domain.usecases.coordinates

import com.example.coordinatebook.domain.CoordinatesRepository
import com.example.coordinatebook.domain.models.CoordinatesInfo

class GetCoordinatesByIdUseCase(val repository: CoordinatesRepository) {
    suspend fun execute(worldId: Int): MutableList<CoordinatesInfo> {
        return repository.getCoordinatesByWorldId(worldId)
    }
}