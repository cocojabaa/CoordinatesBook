package com.example.coordinatebook.domain

import com.example.coordinatebook.domain.models.CoordinatesInfo

interface CoordinatesRepository {
    suspend fun getCoordinatesById(worldId: Int): MutableList<CoordinatesInfo>
    suspend fun addCoordinates(coordinatesInfo: CoordinatesInfo): Boolean
    suspend fun deleteCoordinates(coordinatesInfo: CoordinatesInfo): Boolean
    suspend fun deleteAllCoordinatesById(worldId: Int): Boolean
}