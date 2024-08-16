package com.example.coordinatebook.domain.usecases.coordinates

import com.example.coordinatebook.domain.CoordinatesRepository
import com.example.coordinatebook.domain.models.CoordinatesInfo

class EditCoordinatesUseCase(val repository: CoordinatesRepository) {
    suspend fun execute(newCoordinatesInfo: CoordinatesInfo): Boolean {
        return repository.editCoordinates(newCoordinatesInfo)
    }
}