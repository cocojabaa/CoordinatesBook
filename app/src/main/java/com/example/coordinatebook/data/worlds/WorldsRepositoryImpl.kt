package com.example.coordinatebook.data.worlds

import android.content.Context
import android.util.Log
import com.example.coordinatebook.data.MainDatabase
import com.example.coordinatebook.domain.WorldsRepository
import com.example.coordinatebook.domain.models.WorldInfo

class WorldsRepositoryImpl(val context: Context): WorldsRepository {
    private val db = MainDatabase.getDb(context)

    override suspend fun getAllWorlds(): MutableList<WorldInfo> {
        val worldsEntities = db.getWorldsDao().getAllWorlds()
        var worlds = mutableListOf<WorldInfo>()
        worldsEntities.forEach {
            worlds.add(
                WorldInfo(it.id, it.name, it.description)
            )
        }
        return worlds
    }

    override suspend fun deleteWorld(worldId: Int?): Boolean {
        try {
            db.getWorldsDao().deleteWorld(worldId)
            return true
        } catch (ex: Exception) {
            Log.e("My", "DELETE WORLD ERROR: ${ex.message}")
            return false
        }
    }

    override suspend fun addWorld(worldInfo: WorldInfo): Int? {
        try {
            val entity = WorldEntity(worldInfo.id, worldInfo.name, worldInfo.description)
            db.getWorldsDao().addWorld(entity)
            return db.getWorldsDao().getWorldByName(worldInfo.name).id
        } catch (ex: Exception) {
            Log.e("My", "ADD WORLD ERROR: ${ex.message}")
            return null
        }
    }

    override suspend fun getWorldIdByName(worldName: String): Int? {
        try {
            val worldEntity = db.getWorldsDao().getWorldByName(worldName)
            return worldEntity.id
        } catch (ex: Exception) {
            Log.e("My", "GET WORLD ERROR: ${ex.message}")
            return null
        }
    }

    override suspend fun editWorld(newWorldInfo: WorldInfo): Boolean {
        try {
            db.getWorldsDao().editWorld(newWorldInfo.id, newWorldInfo.name, newWorldInfo.description)
            return true
        } catch (ex: Exception) {
            Log.e("My", "GET WORLD ERROR: ${ex.message}")
            return false
        }
    }
}