package com.markoid.parky.home.domain.usecases.response

import android.util.Log
import com.google.android.gms.maps.model.LatLng
import com.markoid.parky.core.presentation.extensions.latLng
import com.markoid.parky.position.data.repositories.TrackingRepository
import com.markoid.parky.position.presentation.managers.PositionManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.joda.time.Duration
import javax.inject.Inject
import kotlin.math.roundToLong

class GetUserLocationUpdatesUseCase
@Inject constructor(
    private val trackingRepository: TrackingRepository
) {

    fun subscribeToLocationUpdates(parkingSpotLocation: LatLng): Flow<LocationUpdatesResponse> =
        trackingRepository
            .getRealTimeLocation()
            .map { Pair(it, getDistance(it.latLng, parkingSpotLocation)) }
            .map {
                Log.d("leissue", "Distance: ${it.second}, Speed: ${it.first.speed}, Time: ${it.second / it.first.speed / 60}")
                LocationUpdatesResponse(
                    getDistanceFormatted(it.second),
                    it.first.latLng,
                    getTimeFormatted(it.second, it.first.speed),
                    getSpeedInKph(it.first.speed)
                )
            }

    private fun getSpeedInKph(speed: Float): String =
        StringBuilder().append((speed * 3.6).roundToLong()).append(" ").append("km/hr").toString()

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
        if (speed < 0.28) return "INF"
        val timeInSeconds = (distanceInMeters / speed).toLong()
        val timeDuration = Duration.standardSeconds(timeInSeconds)
        return when {
            timeDuration.standardDays >= 1 -> StringBuilder()
                .append(timeDuration.standardDays)
                .append(" ")
                .append("d")
                .toString()
            timeDuration.standardHours >= 1 -> StringBuilder()
                .append(timeDuration.standardHours)
                .append(" ")
                .append("hrs")
                .toString()
            timeDuration.standardMinutes >= 1 -> StringBuilder()
                .append(timeDuration.standardMinutes)
                .append(" ")
                .append("min")
                .toString()
            timeDuration.standardSeconds >= 1 -> StringBuilder()
                .append(timeDuration.standardSeconds)
                .append(" ")
                .append("sec")
                .toString()
            else -> "-"
        }
    }
}
