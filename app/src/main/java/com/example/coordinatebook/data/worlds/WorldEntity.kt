package com.example.coordinatebook.data.worlds

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "worlds")
data class WorldEntity (

    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,

    @ColumnInfo(name = "name")
    var name: String,

    @ColumnInfo(name = "description")
    var description: String,
)