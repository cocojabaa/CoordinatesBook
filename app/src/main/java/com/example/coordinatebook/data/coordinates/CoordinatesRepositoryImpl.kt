package com.example.coordinatebook.data.coordinates

import android.content.Context
import android.util.Log
import com.example.coordinatebook.domain.CoordinatesRepository
import com.example.coordinatebook.domain.models.CoordinatesInfo

class CoordinatesRepositoryImpl(context: Context): CoordinatesRepository {
    val db = CoordinatesDatabase.getDb(context)

    override suspend fun getCoordinatesById(worldId: Int): MutableList<CoordinatesInfo> {
        val coordinatesEntityes = db.getDao().getCoordinatesById(worldId)
        var coordinatesInfo = mutableListOf<CoordinatesInfo>()
        coordinatesEntityes.forEach {
            coordinatesInfo.add(
                CoordinatesInfo(
                    it.worldId, it.description, it.dimension, it.x, it.y, it.z
                )
            )
        }
        return coordinatesInfo
    }

    override suspend fun addCoordinates(coordinatesInfo: CoordinatesInfo): Boolean {
        try {
            val coordinatesEntity = CoordinatesEntity(
                worldId = coordinatesInfo.worldId,
                description = coordinatesInfo.description,
                dimension = coordinatesInfo.dimension,
                x = coordinatesInfo.x,
                y = coordinatesInfo.y,
                z = coordinatesInfo.z,
            )
            db.getDao().addCoordinates(coordinatesEntity)
            return true
        } catch (ex: Exception) {
            Log.e("My", "ADD COORDINATERs ERROR: ${ex.message}")
            return false
        }
    }

    override suspend fun deleteCoordinates(coordinatesInfo: CoordinatesInfo): Boolean {
        TODO("Not yet implemented")
    }
}