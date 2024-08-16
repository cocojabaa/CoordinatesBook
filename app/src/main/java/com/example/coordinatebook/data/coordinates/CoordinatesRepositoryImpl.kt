package com.example.coordinatebook.data.coordinates

import android.content.Context
import android.util.Log
import com.example.coordinatebook.data.MainDatabase
import com.example.coordinatebook.domain.CoordinatesRepository
import com.example.coordinatebook.domain.models.CoordinatesInfo
import com.example.coordinatebook.domain.models.Dimensions

class CoordinatesRepositoryImpl(context: Context): CoordinatesRepository {
    val db = MainDatabase.getDb(context)

    override suspend fun getCoordinatesByWorldId(worldId: Int): MutableList<CoordinatesInfo> {
        val coordinatesEntities = db.getCoordinatesDao().getCoordinatesById(worldId)
        var coordinatesInfoList = mutableListOf<CoordinatesInfo>()
        coordinatesEntities.forEach {
            lateinit var dimensionValue: Dimensions
            when (it.dimension) {
                Dimensions.UpperWorld.value -> { dimensionValue = Dimensions.UpperWorld }
                Dimensions.Nether.value -> { dimensionValue = Dimensions.Nether }
                Dimensions.End.value -> { dimensionValue = Dimensions.End }
            }
            coordinatesInfoList.add(
                CoordinatesInfo(
                    it.id, it.worldId, it.description, dimensionValue, it.x, it.y, it.z
                )
            )
        }
        return coordinatesInfoList
    }

    override suspend fun addCoordinates(coordinatesInfo: CoordinatesInfo): Int? {
        try {
            val coordinatesEntity = CoordinatesEntity(
                worldId = coordinatesInfo.worldId,
                description = coordinatesInfo.description,
                dimension = coordinatesInfo.dimension.value,
                x = coordinatesInfo.x,
                y = coordinatesInfo.y,
                z = coordinatesInfo.z,
            )
            Log.i("My", "REPOS ID=${coordinatesEntity.worldId} DES=${coordinatesEntity.description} DIM=${coordinatesEntity.dimension} X=${coordinatesEntity.x} Y=${coordinatesEntity.y} Z=${coordinatesEntity.z}")
            db.getCoordinatesDao().addCoordinates(coordinatesEntity)
            return db.getCoordinatesDao().getCoordinatesId(
                coordinatesInfo.worldId,
                coordinatesInfo.description,
                coordinatesInfo.dimension.value,
                coordinatesInfo.x,
                coordinatesInfo.z
            ).id
        } catch (ex: Exception) {
            Log.e("My", "ADD COORDINATES ERROR: ${ex.message}")
            return null
        }
    }

    override suspend fun deleteCoordinates(coordinatesId: Int): Boolean {
        try {
            db.getCoordinatesDao().deleteCoordinates(coordinatesId)
            return true
        } catch(ex: Exception) {
            Log.e("My", "DELETE COORDINATES ERROR: ${ex.message}")
            return false
        }
    }

    override suspend fun deleteAllCoordinatesByWorldId(worldId: Int): Boolean {
        try {
            db.getCoordinatesDao().deleteAllCoordinatesById(worldId)
            return true
        } catch(ex: Exception) {
            Log.e("My", "DELETE ALL COORDINATES ERROR: ${ex.message}")
            return false
        }
    }

    override suspend fun editCoordinates(newCoordinatesInfo: CoordinatesInfo): Boolean {
        try {
            db.getCoordinatesDao().editCoordinatesById(
                newCoordinatesInfo.id,
                newCoordinatesInfo.description,
                newCoordinatesInfo.dimension.name,
                newCoordinatesInfo.x,
                newCoordinatesInfo.y,
                newCoordinatesInfo.z
            )
            return true
        } catch (ex: Exception) {
            return false
        }
    }

}