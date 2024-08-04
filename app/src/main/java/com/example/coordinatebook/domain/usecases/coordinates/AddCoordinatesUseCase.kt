package com.example.coordinatebook.domain.usecases.coordinates

import com.example.coordinatebook.domain.CoordinatesRepository
import com.example.coordinatebook.domain.models.CoordinatesInfo

class AddCoordinatesUseCase(val repository: CoordinatesRepository) {
    suspend fun execute(coordinatesInfo: CoordinatesInfo): Boolean {
        return repository.addCoordinates(coordinatesInfo)
    }
}