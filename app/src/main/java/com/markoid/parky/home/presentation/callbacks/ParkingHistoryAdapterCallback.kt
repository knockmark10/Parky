package com.markoid.parky.home.presentation.callbacks

interface ParkingHistoryAdapterCallback {
    fun onDeleteParkingSpot(parkingSpotId: Long)
    fun onGoToUserLocation()
    fun onDisplayEmptyState()
}
