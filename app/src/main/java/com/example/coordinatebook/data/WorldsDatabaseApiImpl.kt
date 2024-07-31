package com.example.coordinatebook.data

import android.content.Context
import android.util.Log
import com.example.coordinatebook.domain.WorldsDatabaseApi
import com.example.coordinatebook.domain.models.WorldInfo

class WorldsDatabaseApiImpl(val context: Context): WorldsDatabaseApi {
    private val dao = WorldsDatabase.getDb(context).getDao()

    override fun getAllWorlds(): MutableList<WorldInfo> {
        val worldsEntities = dao.getAllWorlds()
        var worlds = mutableListOf<WorldInfo>()
        worldsEntities.forEach {
            worlds.add(
                WorldInfo(it.id, it.name, it.description)
            )
        }
        return worlds
    }

    override fun deleteWorld(worldInfo: WorldInfo) {
        try {
            val entity = WorldEntity(worldInfo.id, worldInfo.name, worldInfo.description)
            dao.deleteWorld(entity)
        } catch (ex: Exception) {
            Log.e("My", "DELETE WORLD ERROR: ${ex.message}")
        }
    }

    override fun addWorld(worldInfo: WorldInfo){
        try {
            val entity = WorldEntity(worldInfo.id, worldInfo.name, worldInfo.description)
            dao.addWorld(entity)
        } catch (ex: Exception) {
            Log.e("My", "ADD WORLD ERROR: ${ex.message}")
        }
    }
}