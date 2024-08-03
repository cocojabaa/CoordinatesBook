package com.example.coordinatebook.domain

import com.example.coordinatebook.domain.models.CoordinatesInfo

interface CoordinatesRepository {
    suspend fun getCoordinates(worldId: Int): List<CoordinatesInfo>
    suspend fun addCoordinates(worldId: Int): Boolean
    suspend fun deleteCoordinates(worldId: Int): Boolean
}