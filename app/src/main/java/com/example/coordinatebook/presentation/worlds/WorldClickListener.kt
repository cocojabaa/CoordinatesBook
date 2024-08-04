package com.example.coordinatebook.presentation.worlds

import com.example.coordinatebook.domain.models.WorldInfo

interface WorldClickListener {
    fun onWorldClick(worldInfo: WorldInfo)
    fun onWorldLongClick(worldInfo: WorldInfo)
}