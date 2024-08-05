package com.example.coordinatebook.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.coordinatebook.data.coordinates.CoordinatesDao
import com.example.coordinatebook.data.coordinates.CoordinatesEntity
import com.example.coordinatebook.data.worlds.WorldEntity
import com.example.coordinatebook.data.worlds.WorldsDao


@Database(
    entities = [WorldEntity::class, CoordinatesEntity::class],
    version = 1
)
abstract class MainDatabase: RoomDatabase() {
    abstract fun getWorldsDao(): WorldsDao
    abstract fun getCoordinatesDao(): CoordinatesDao

    companion object {
        fun getDb(context: Context): MainDatabase {
            return Room.databaseBuilder(
                context.applicationContext,
                MainDatabase::class.java,
                "main.db"
            ).build()
        }
    }
}