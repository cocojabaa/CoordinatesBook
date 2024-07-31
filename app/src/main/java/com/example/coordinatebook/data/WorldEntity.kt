package com.example.coordinatebook.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "worlds")
data class WorldEntity (

    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,

    @ColumnInfo(name = "Name")
    var name: String,

    @ColumnInfo(name = "Description")
    var description: String?,
)