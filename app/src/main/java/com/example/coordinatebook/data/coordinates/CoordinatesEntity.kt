package com.example.coordinatebook.data.coordinates

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "coordinates")
data class CoordinatesEntity (

    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,

    @ColumnInfo(name="worldId")
    val worldId: Int,

    @ColumnInfo(name="description")
    val description: String,

    @ColumnInfo(name="dimension")
    val dimension: String,

    @ColumnInfo(name="x")
    val x: Int,

    @ColumnInfo(name="y")
    val y: Int,

    @ColumnInfo(name="z")
    val z: Int,
)