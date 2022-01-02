package com.markoid.parky.position.data.repositories

import android.location.Location
import com.google.android.gms.maps.model.LatLng
import com.markoid.parky.position.data.entities.PositionEntity
import kotlinx.coroutines.flow.Flow

interface TrackingRepository {
    suspend fun translateCoordinates(location: LatLng): PositionEntity
    suspend fun getCurrentLocation(): Location
    suspend fun getLocationWithSamples(samples: Int): Location
    fun getRealTimeLocation(): Flow<Location>
}
