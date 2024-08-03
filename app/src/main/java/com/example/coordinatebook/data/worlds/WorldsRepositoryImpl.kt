package com.example.coordinatebook.data.worlds

import android.content.Context
import android.util.Log
import com.example.coordinatebook.domain.WorldsRepository
import com.example.coordinatebook.domain.models.WorldInfo

class WorldsRepositoryImpl(val context: Context): WorldsRepository {
    private val db = WorldsDatabase.getDb(context)

    override suspend fun getAllWorlds(): MutableList<WorldInfo> {
        val worldsEntities = db.getDao().getAllWorlds()
        var worlds = mutableListOf<WorldInfo>()
        worldsEntities.forEach {
            worlds.add(
                WorldInfo(it.id, it.name, it.description)
            )
        }
        return worlds
    }

    override suspend fun deleteWorld(worldName: String): Boolean {
        try {
            db.getDao().deleteWorld(worldName)
            return true
        } catch (ex: Exception) {
            Log.e("My", "DELETE WORLD ERROR: ${ex.message}")
            return false
        }
    }

    override suspend fun addWorld(worldInfo: WorldInfo): Boolean{
        try {
            val entity = WorldEntity(worldInfo.id, worldInfo.name, worldInfo.description)
            db.getDao().addWorld(entity)
            return true
        } catch (ex: Exception) {
            Log.e("My", "ADD WORLD ERROR: ${ex.message}")
            return false
        }
    }
}