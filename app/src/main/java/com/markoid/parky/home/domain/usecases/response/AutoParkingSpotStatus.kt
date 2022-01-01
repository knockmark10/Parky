package com.markoid.parky.home.domain.usecases.response

import com.markoid.parky.home.domain.usecases.request.ParkingSpotRequest

/**
 * This wil lbe the result status after trying to save parking spot automatically.
 */
sealed class AutoParkingSpotStatus {
    /**
     * Indicates that there was a bluetooth disconnection event received, but at least one condition
     * wasn't met to save the parking spot.
     *
     * There is no action required from the user
     */
    object SkipDisconnectionEvent : AutoParkingSpotStatus()

    /**
     * Indicates that the service tried to saved the parking spot automatically, but the location
     * received did not meet the expectations.
     *
     * User needs to register the parking spot automatically.
     */
    object LocationAccuracyNotMet : AutoParkingSpotStatus()

    /**
     * Indicates that the service tried to saved the parking spot automatically, but some required
     * data is missing.
     *
     * User needs to complete the parking spot data.
     */
    data class MissingData(val request: ParkingSpotRequest) : AutoParkingSpotStatus()

    /**
     * Indicates that the service saved the parking spot automatically successfully.
     *
     * There is no action required from the user
     */
    data class ParkingSpotSavedAutomatically(val parkingSpotId: Long) : AutoParkingSpotStatus()
}
