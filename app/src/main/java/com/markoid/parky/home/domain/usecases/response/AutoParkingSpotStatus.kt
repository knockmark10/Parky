package com.markoid.parky.home.domain.usecases.response

import com.markoid.parky.home.domain.usecases.request.ParkingSpotRequest

sealed class AutoParkingSpotStatus {
    object SkipDisconnectionEvent : AutoParkingSpotStatus()
    data class MissingData(val request: ParkingSpotRequest) : AutoParkingSpotStatus()
    data class ParkingSpotSavedAutomatically(val parkingSpotId: Long) : AutoParkingSpotStatus()
}
