package com.example.coordinatebook.data.coordinates

import android.content.Context
import android.util.Log
import com.example.coordinatebook.domain.CoordinatesRepository
import com.example.coordinatebook.domain.models.CoordinatesInfo
import com.example.coordinatebook.domain.models.Dimensions

class CoordinatesRepositoryImpl(context: Context): CoordinatesRepository {
    val db = CoordinatesDatabase.getDb(context)

    override suspend fun getCoordinatesById(worldId: Int): MutableList<CoordinatesInfo> {
        val coordinatesEntityes = db.getDao().getCoordinatesById(worldId)
        var coordinatesInfoList = mutableListOf<CoordinatesInfo>()
        coordinatesEntityes.forEach {
            lateinit var dimensionValue: Dimensions
            when (it.dimension) {
                Dimensions.UpperWorld.value -> { dimensionValue = Dimensions.UpperWorld }
                Dimensions.Nether.value -> { dimensionValue = Dimensions.Nether }
                Dimensions.End.value -> { dimensionValue = Dimensions.End }
            }
            coordinatesInfoList.add(
                CoordinatesInfo(
                    it.worldId, it.description, dimensionValue, it.x, it.y, it.z
                )
            )
        }
        return coordinatesInfoList
    }

    override suspend fun addCoordinates(coordinatesInfo: CoordinatesInfo): Boolean {
        try {
            val coordinatesEntity = CoordinatesEntity(
                worldId = coordinatesInfo.worldId,
                description = coordinatesInfo.description,
                dimension = coordinatesInfo.dimension.value,
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