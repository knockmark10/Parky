package com.markoid.parky.position.data.repositories

import android.location.Location
import com.google.android.gms.maps.model.LatLng
import com.markoid.parky.position.data.datasources.TrackingDataSource
import com.markoid.parky.position.data.entities.PositionEntity
import com.markoid.parky.position.data.mappers.toPositionEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TrackingRepositoryImpl @Inject constructor(
    private val trackingDataSource: TrackingDataSource
) : TrackingRepository {

    override suspend fun translateCoordinates(location: LatLng): PositionEntity =
        translatePositionWithAndroidApi(location)

    override suspend fun getCurrentLocation(): LatLng {
        val location = this.trackingDataSource.getCurrentLocation()
        return LatLng(location?.latitude ?: 0.0, location?.longitude ?: 0.0)
    }

    override fun getRealTimeLocation(): Flow<Location> =
        trackingDataSource.getRealTimeLocation()

    /**
     * Translates the provided [LatLng] with the Android Library. This may fall in several cases,
     * so we're going to fallback on Position Stack Server.
     */
    private suspend fun translatePositionWithAndroidApi(location: LatLng): PositionEntity = try {
        this.trackingDataSource.translateCoordinatesWithAndroidApi(location)
            .firstOrNull()
            ?.toPositionEntity() ?: translatePositionWithPositionStack(location)
    } catch (e: Throwable) {
        e.printStackTrace()
        translatePositionWithPositionStack(location)
    }

    /**
     * If Android Library is not available, let's try with Position Stack Server, which has a limit
     * and an api_key. Fallback to Big Data Cloud when this fails.
     */
    private suspend fun translatePositionWithPositionStack(location: LatLng): PositionEntity = try {
        val formattedLocation = String.format("%s,%s", location.latitude, location.longitude)
        this.trackingDataSource.translateCoordinatesWithPositionStack(formattedLocation)
            .data
            .firstOrNull()
            ?.toPositionEntity() ?: translatePositionWithBigDataCloud(location)
    } catch (e: Throwable) {
        e.printStackTrace()
        translatePositionWithBigDataCloud(location)
    }

    /**
     * This is the last fallback available at the moment. This is totally free and should work well
     * with most cases. The information provided is somewhat limited, but it's better than nothing.
     */
    private suspend fun translatePositionWithBigDataCloud(location: LatLng): PositionEntity =
        this.trackingDataSource.translateCoordinatesWithBigDataCloud(location)
            .toPositionEntity()
}
