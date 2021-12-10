package com.markoid.parky.position.domain.usecases

import com.google.android.gms.maps.model.LatLng
import com.markoid.parky.core.domain.usecases.UseCase
import com.markoid.parky.permissions.presentation.controllers.LocationPermissionController
import com.markoid.parky.permissions.presentation.enums.LocationPermissions
import com.markoid.parky.position.data.entities.PositionEntity
import com.markoid.parky.position.data.repositories.TrackingRepository
import com.markoid.parky.position.domain.exceptions.GpsNotAvailableException
import com.markoid.parky.position.domain.exceptions.InvalidPositionException
import com.markoid.parky.position.presentation.extensions.isValid
import javax.inject.Inject

class GetCurrentLocationUseCase
@Inject constructor(
    private val trackingRepository: TrackingRepository,
    private val locationPermissionController: LocationPermissionController
) : UseCase<PositionEntity, Unit>() {

    override suspend fun onExecute(request: Unit): PositionEntity {
        // Make sure that location permissions are handled
        val locationPermissionGranted =
            locationPermissionController.onRequestPermission(LocationPermissions.RegularLocation)
        return if (locationPermissionGranted) {
            getCurrentLocation()
        } else {
            throw GpsNotAvailableException()
        }
    }

    private suspend fun getCurrentLocation(): PositionEntity {
        // Get current location
        val currentLocation: LatLng = trackingRepository.getCurrentLocation()
        // Validate that we are getting valid coordinates
        if (currentLocation.isValid.not()) throw InvalidPositionException()
        // Translate the coordinates that we got
        return trackingRepository.translateCoordinates(currentLocation)
    }
}
