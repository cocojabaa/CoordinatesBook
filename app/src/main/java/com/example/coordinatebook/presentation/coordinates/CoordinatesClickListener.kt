package com.example.coordinatebook.presentation.coordinates

import com.example.coordinatebook.domain.models.CoordinatesInfo

interface CoordinatesClickListener {
    fun onCoordinatesLongClick(coordinatesInfo: CoordinatesInfo)
}