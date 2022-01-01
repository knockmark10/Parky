package com.markoid.parky.home.domain.usecases

import android.content.res.Resources
import com.google.android.gms.maps.model.LatLng
import com.markoid.parky.R
import com.markoid.parky.core.domain.usecases.UseCase
import com.markoid.parky.core.presentation.extensions.latLng
import com.markoid.parky.home.domain.repositories.ExclusionZoneRepository
import com.markoid.parky.home.domain.usecases.request.ParkingSpotRequest
import com.markoid.parky.home.domain.usecases.response.AutoParkingSpotStatus
import com.markoid.parky.home.domain.usecases.response.ParkingValidationStatus
import com.markoid.parky.position.data.repositories.TrackingRepository
import com.markoid.parky.position.presentation.managers.PositionManager
import com.markoid.parky.settings.presentation.managers.DevicePreferences
import org.joda.time.DateTime
import javax.inject.Inject

/**
 * This use case will be used to save parking spot automatically when bluetooth gets disconnected.
 */
class SaveParkingSpotAutoUseCase
@Inject constructor(
    private val devicePreferences: DevicePreferences,
    private val exclusionZoneRepository: ExclusionZoneRepository,
    private val saveParkingInDbUseCase: SaveParkingInDbUseCase,
    private val resources: Resources,
    private val trackingRepository: TrackingRepository,
    private val validateNewParkingUseCase: ValidateNewParkingUseCase
) : UseCase<AutoParkingSpotStatus, String>() {

    private val any: String
        get() = resources.getString(R.string.any)

    /**
     * This method will save the parking spot automatically upon bluetooth disconnection.
     * To do so the following conditions need to be met:
     *
     * 1) There is no active parking spot at the moment.
     * 2) Bluetooth device name matches the one saved in preferences.
     * 3) Current location is not within one of the exclusion zones
     *
     * If bluetooth device name is 'any', then any device disconnected would suffice to save
     * the parking spot automatically.
     *
     * @param request - The bluetooth device name that was just disconnected.
     */
    override suspend fun onExecute(request: String): AutoParkingSpotStatus {
        // Check initial constraints
        if (isThereAnyParkingSpotActive() || bluetoothDeviceDoesNotMatch(request))
            return AutoParkingSpotStatus.SkipDisconnectionEvent
        // Get user's current location
        val location = trackingRepository.getCurrentLocation()
        // Check accuracy
        if (location.accuracy > devicePreferences.locationAccuracy)
            return AutoParkingSpotStatus.LocationAccuracyNotMet
        // Extract coordinates from location
        val coordinates = location.latLng
        // Check exclusion zones. If we're inside any, we need to skip this
        if (isInsideExclusionZone(coordinates)) return AutoParkingSpotStatus.SkipDisconnectionEvent
        // Translate such location
        val positionEntity = trackingRepository.translateCoordinates(coordinates)
        // Build request with location fetched
        val parkingRequest = ParkingSpotRequest(
            address = positionEntity.streetAddress,
            alarmTime = null,
            color = "",
            hourRate = devicePreferences.hourRate,
            floorNumber = "",
            floorType = "",
            latitude = location.latitude,
            longitude = location.longitude,
            lotIdentifier = "",
            parkingTime = DateTime.now(),
            parkingTimeFormatted = positionEntity.dateFormatted,
            parkingType = devicePreferences.favoriteParkingType
        )
        // Validate built data
        val status = validateNewParkingUseCase.onExecute(parkingRequest)
        // If validations fail, return the parking request that needs to be completed
        if (status !is ParkingValidationStatus.Success)
            return AutoParkingSpotStatus.MissingData(parkingRequest)
        // If everything went ok, save the spot into database
        val parkingSpotId: Long = saveParkingInDbUseCase.onExecute(parkingRequest)
        // Set flag on preferences
        devicePreferences.isParkingSpotActive = true
        // Return success state
        return AutoParkingSpotStatus.ParkingSpotSavedAutomatically(parkingSpotId)
    }

    private fun isThereAnyParkingSpotActive(): Boolean =
        devicePreferences.isParkingSpotActive

    private fun bluetoothDeviceDoesNotMatch(disconnectedDevice: String): Boolean =
        devicePreferences.bluetoothDevice != any &&
            devicePreferences.bluetoothDevice != disconnectedDevice

    private suspend fun isInsideExclusionZone(currentLocation: LatLng): Boolean {
        val zones = exclusionZoneRepository.getExclusionZones()
        return zones.any {
            // Get the distance between current location and the circle one
            val distance = PositionManager.getDistanceFromLocations(currentLocation, it.location)
            // If distance is lower or equals than the radius, it means that we're inside an exclusion zone
            distance <= it.radius
        }
    }
}
