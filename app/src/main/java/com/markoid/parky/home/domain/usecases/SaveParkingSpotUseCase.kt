package com.markoid.parky.home.domain.usecases

import com.markoid.parky.core.domain.usecases.UseCase
import com.markoid.parky.home.domain.usecases.request.ParkingSpotRequest
import com.markoid.parky.home.domain.usecases.response.ParkingValidationStatus
import com.markoid.parky.settings.presentation.managers.DevicePreferences
import javax.inject.Inject

class SaveParkingSpotUseCase
@Inject constructor(
    private val devicePreferences: DevicePreferences,
    private val saveParkingInDbUseCase: SaveParkingInDbUseCase,
    private val validateNewParkingUseCase: ValidateNewParkingUseCase
) : UseCase<ParkingValidationStatus, ParkingSpotRequest>() {

    override suspend fun onExecute(request: ParkingSpotRequest): ParkingValidationStatus {
        // Get validation status
        val validationStatus = validateNewParkingUseCase.onExecute(request)
        // If validations fail, return the validation status
        if (validationStatus != ParkingValidationStatus.Success) return validationStatus
        // Save parking spot into database
        saveParkingInDbUseCase.onExecute(request)
        // Set flag on preferences
        devicePreferences.isParkingSpotActive = true
        return ParkingValidationStatus.Success
    }
}
