package com.example.coordinatebook.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [WorldEntity::class], version = 1)
abstract class WorldsDatabase: RoomDatabase() {
    abstract fun getDao(): WorldsDao

    companion object {
        fun getDb(context: Context): WorldsDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                WorldsDatabase::class.java,
                "worlds.db"
            ).build()
        }
    }
}