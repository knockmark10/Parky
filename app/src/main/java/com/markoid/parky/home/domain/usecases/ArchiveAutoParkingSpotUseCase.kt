package com.markoid.parky.home.domain.usecases

import android.content.res.Resources
import com.google.android.gms.maps.model.LatLng
import com.markoid.parky.R
import com.markoid.parky.core.domain.usecases.UseCase
import com.markoid.parky.core.presentation.extensions.latLng
import com.markoid.parky.home.domain.repositories.ParkingRepository
import com.markoid.parky.position.data.repositories.TrackingRepository
import com.markoid.parky.position.presentation.managers.PositionManager
import com.markoid.parky.settings.presentation.managers.DevicePreferences
import javax.inject.Inject

class ArchiveAutoParkingSpotUseCase
@Inject constructor(
    private val archiveParkingSpotUseCase: ArchiveParkingSpotUseCase,
    private val devicePreferences: DevicePreferences,
    private val parkingRepository: ParkingRepository,
    private val resources: Resources,
    private val stopBluetoothServiceUseCase: StopBluetoothServiceUseCase,
    private val trackingRepository: TrackingRepository
) : UseCase<Boolean, String>() {

    private val any: String
        get() = resources.getString(R.string.any)

    override suspend fun onExecute(request: String): Boolean {
        // Check the bluetooth device we just connected to
        if (bluetoothDeviceDoesNotMatch(request)) return false
        // Get current active parking spot. If there is none, return null
        val activeParkingSpot = parkingRepository.getActiveParkingSpot() ?: return false
        // Get user's current location
        val location = trackingRepository.getLocationWithSamples(5)
        // Extract coordinates from user's location
        val userCoordinates = location.latLng
        // Extract coordinates from parking spot
        val parkingSpotCoordinates = LatLng(activeParkingSpot.latitude, activeParkingSpot.longitude)
        // Calculate the distance
        val distance = PositionManager.getDistanceFromLocations(
            userCoordinates,
            parkingSpotCoordinates
        )
        // If distance is greater than the one allowed, do nothing
        if (distance > devicePreferences.locationAccuracy) return false
        // At this point, archive your parking spot
        archiveParkingSpotUseCase.onExecute(activeParkingSpot)
        // Finally, stop bluetooth service
        stopBluetoothServiceUseCase.onExecute(Unit)
        return true
    }

    private fun bluetoothDeviceDoesNotMatch(disconnectedDevice: String): Boolean =
        devicePreferences.bluetoothDevice != any &&
            devicePreferences.bluetoothDevice != disconnectedDevice
}
