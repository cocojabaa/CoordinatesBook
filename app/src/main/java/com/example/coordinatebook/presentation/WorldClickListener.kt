package com.example.coordinatebook.presentation

import com.example.coordinatebook.domain.models.WorldInfo

interface WorldClickListener {
    fun onWorldClick(worldInfo: WorldInfo)
}