package com.example.coordinatebook.data

import android.content.Context
import android.util.Log
import androidx.lifecycle.asLiveData
import com.example.coordinatebook.domain.WorldsDatabaseApi
import com.example.coordinatebook.domain.models.WorldInfo

class WorldsDatabaseApiImpl(val context: Context): WorldsDatabaseApi {
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

    override suspend fun deleteWorld(worldInfo: WorldInfo): Boolean {
        try {
            val entity = WorldEntity(worldInfo.id, worldInfo.name, worldInfo.description)
            db.getDao().deleteWorld(entity)
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