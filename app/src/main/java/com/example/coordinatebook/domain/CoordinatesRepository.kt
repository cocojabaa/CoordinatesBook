package com.example.coordinatebook.domain

import com.example.coordinatebook.domain.models.CoordinatesInfo

interface CoordinatesRepository {
    suspend fun getCoordinatesByWorldId(worldId: Int): MutableList<CoordinatesInfo>
    suspend fun addCoordinates(coordinatesInfo: CoordinatesInfo): Int?
    suspend fun deleteCoordinates(coordinatesId: Int): Boolean
    suspend fun deleteAllCoordinatesByWorldId(worldId: Int): Boolean
}
