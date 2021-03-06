package com.markoid.parky.position.data.datasources

import android.location.Address
import android.location.Location
import com.google.android.gms.maps.model.LatLng
import com.markoid.parky.position.data.entities.bigdatacloud.BigDataCloudResponse
import com.markoid.parky.position.data.entities.positionstack.PositionStackResponse
import kotlinx.coroutines.flow.Flow

interface TrackingDataSource {
    suspend fun getCurrentLocation(): Location
    suspend fun getLocationWithSamples(samples: Int): Location
    fun getRealTimeLocation(): Flow<Location>
    suspend fun translateCoordinatesWithAndroidApi(location: LatLng): List<Address>
    suspend fun translateCoordinatesWithBigDataCloud(location: LatLng): BigDataCloudResponse
    suspend fun translateCoordinatesWithPositionStack(latLng: String): PositionStackResponse
    suspend fun translateLocationNameWithPositionStack(latLng: String): PositionStackResponse
}
