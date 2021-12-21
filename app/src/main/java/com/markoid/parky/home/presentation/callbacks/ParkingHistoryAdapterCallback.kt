package com.markoid.parky.home.presentation.callbacks

import com.markoid.parky.home.data.entities.ParkingSpotEntity

interface ParkingHistoryAdapterCallback {
    fun onRequestDeleteParkingSpot(spot: ParkingSpotEntity)
    fun onGoToUserLocation()
    fun onDisplayEmptyState()
}
