package com.example.coordinatebook.presentation.coordinates

import com.example.coordinatebook.domain.models.CoordinatesInfo

interface CoordinatesClickListener {
    fun onCoordinatesClick(coordinatesInfo: CoordinatesInfo)
    fun onCoordinatesLongClick(coordinatesInfo: CoordinatesInfo)
}