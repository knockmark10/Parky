package com.markoid.parky.home.domain.usecases.response

import android.location.Location
import com.markoid.parky.home.data.entities.ParkingSpotEntity

data class ActiveParkingSpotResponse(
    val distance: String,
    val userLocation: Location,
    val parkingSpot: ParkingSpotEntity,
    val time: String
)
