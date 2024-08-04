package com.example.coordinatebook.data.coordinates

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.coordinatebook.data.worlds.WorldsDatabase

@Database(entities = [CoordinatesEntity::class], version = 1)
abstract class CoordinatesDatabase: RoomDatabase() {
    abstract fun getDao(): CoordinatesDao

    companion object {
        fun getDb(context: Context): CoordinatesDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                CoordinatesDatabase::class.java,
                "coordinates.db"
            ).build()
        }
    }
}