package com.markoid.parky.home.domain.usecases.request

import android.net.Uri
import org.joda.time.DateTime

data class ParkingSpotRequest(
    val address: String,
    val color: String,
    val fare: Double,
    val floorNumber: String,
    val floorType: String,
    val latitude: Double,
    val longitude: Double,
    val lotIdentifier: String,
    val parkingTime: DateTime,
    val parkingTimeFormatted: String,
    val parkingType: String,
    val photo: Uri? = null
)
