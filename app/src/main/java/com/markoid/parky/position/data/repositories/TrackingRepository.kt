package com.markoid.parky.position.data.repositories

import com.google.android.gms.maps.model.LatLng
import com.markoid.parky.position.data.entities.PositionEntity

interface TrackingRepository {
    suspend fun translateCoordinates(location: LatLng): PositionEntity
    suspend fun getCurrentLocation(): LatLng
}
