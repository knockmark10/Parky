package com.markoid.parky.home.domain.usecases.response

import com.google.android.gms.maps.model.LatLng
import com.markoid.parky.core.presentation.extensions.latLng
import com.markoid.parky.position.data.repositories.TrackingRepository
import com.markoid.parky.position.presentation.managers.PositionManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.joda.time.Duration
import javax.inject.Inject

class GetUserLocationUpdatesUseCase
@Inject constructor(
    private val trackingRepository: TrackingRepository
) {

    fun subscribeToLocationUpdates(parkingSpotLocation: LatLng): Flow<LocationUpdatesResponse> =
        trackingRepository
            .getRealTimeLocation()
            .map { Pair(it, getDistance(it.latLng, parkingSpotLocation)) }
            .map {
                LocationUpdatesResponse(
                    getDistanceFormatted(it.second),
                    it.first.latLng,
                    getTimeFormatted(it.second, it.first.speed)
                )
            }

    private fun getDistance(
        currentLocation: LatLng,
        parkingSpotLocation: LatLng
    ): Double = PositionManager.getDistanceFromLocations(currentLocation, parkingSpotLocation)

    private fun getDistanceFormatted(distance: Double): String = if (distance >= 1000.0) {
        StringBuilder()
            .append(String.format("%.2f", distance / 1000.0))
            .append(" ")
            .append("km")
            .toString()
    } else {
        StringBuilder().append(String.format("%.2f", distance)).append(" ").append("m").toString()
    }

    private fun getTimeFormatted(distanceInMeters: Double, speed: Float): String {
        if (speed < 1f) return "-"
        val time = (distanceInMeters / speed).toLong()
        val timeDuration = Duration(time)
        return when {
            timeDuration.standardDays >= 1 -> StringBuilder()
                .append(timeDuration.standardDays)
                .append(" ")
                .append("d")
                .toString()
            timeDuration.standardHours >= 1 -> StringBuilder()
                .append(timeDuration.standardHours)
                .append(" ")
                .append("h")
                .toString()
            timeDuration.standardMinutes >= 1 -> StringBuilder()
                .append(timeDuration.standardMinutes)
                .append(" ")
                .append("m")
                .toString()
            timeDuration.standardSeconds >= 1 -> StringBuilder()
                .append(timeDuration.standardSeconds)
                .append(" ")
                .append("s")
                .toString()
            else -> "-"
        }
    }
}
