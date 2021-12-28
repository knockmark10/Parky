package com.markoid.parky.home.domain.usecases

import android.content.res.Resources
import com.markoid.parky.R
import com.markoid.parky.core.domain.usecases.UseCase
import com.markoid.parky.home.domain.usecases.request.ParkingSpotRequest
import com.markoid.parky.home.domain.usecases.response.AutoParkingSpotStatus
import com.markoid.parky.home.domain.usecases.response.ParkingValidationStatus
import com.markoid.parky.position.data.repositories.TrackingRepository
import com.markoid.parky.settings.presentation.managers.DevicePreferences
import org.joda.time.DateTime
import javax.inject.Inject

/**
 * This use case will be used to save parking spot automatically when bluetooth gets disconnected.
 */
class SaveParkingSpotAutoUseCase
@Inject constructor(
    private val devicePreferences: DevicePreferences,
    private val saveParkingInDbUseCase: SaveParkingInDbUseCase,
    private val resources: Resources,
    private val trackingRepository: TrackingRepository,
    private val validateNewParkingUseCase: ValidateNewParkingUseCase
) : UseCase<AutoParkingSpotStatus, String>() {

    private val none: String
        get() = resources.getString(R.string.none)

    /**
     * This method will save the parking spot automatically upon bluetooth disconnection.
     * To do so the following conditions need to be met:
     *
     * 1) There is no active parking spot at the moment.
     * 2) Bluetooth device name matches the one saved in preferences.
     * 3) Current location is not within one of the exclusion zones
     *
     * If bluetooth device name is 'none', then any device disconnected would suffice to save
     * the parking spot automatically.
     *
     * @param request - The bluetooth device name that was just disconnected.
     */
    override suspend fun onExecute(request: String): AutoParkingSpotStatus {
        if (isThereAnyParkingSpotActive() || bluetoothDeviceDoesNotMatch(request))
            return AutoParkingSpotStatus.SkipDisconnectionEvent

        // TODO: Check exclusion zone (should be an entry on device preferences)

        // Get user's current location
        val location = trackingRepository.getCurrentLocation()
        // Translate such location
        val positionEntity = trackingRepository.translateCoordinates(location)
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
        devicePreferences.bluetoothDevice != none &&
            devicePreferences.bluetoothDevice != disconnectedDevice
}
