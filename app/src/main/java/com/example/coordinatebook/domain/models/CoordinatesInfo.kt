package com.example.coordinatebook.domain.models

data class CoordinatesInfo(
    val worldId: Int,
    val description: String,
    val dimension: Dimensions,
    val x: Int,
    val y: Int?,
    val z: Int,
)
