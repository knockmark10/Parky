package com.markoid.parky.home.domain.usecases

import com.markoid.parky.core.domain.usecases.UseCase
import com.markoid.parky.home.domain.repositories.ParkingRepository
import com.markoid.parky.home.domain.usecases.request.ParkingSpotRequest
import com.markoid.parky.home.domain.usecases.response.ParkingValidationStatus
import javax.inject.Inject

class UpdateParkingSpotUseCase
@Inject constructor(
    private val parkingRepository: ParkingRepository,
    private val validateNewParkingUseCase: ValidateNewParkingUseCase
) : UseCase<ParkingValidationStatus, ParkingSpotRequest>() {

    // TODO: This is fake news. Needs to update properly
    override suspend fun onExecute(request: ParkingSpotRequest): ParkingValidationStatus {
        // Get validation status
        val validationStatus = validateNewParkingUseCase.onExecute(request)
        // If validations fail, return the validation status
        if (validationStatus !is ParkingValidationStatus.Success) return validationStatus
        // Save parking spot into database
        parkingRepository.updateParkingSpotInDatabase(request)
        // Return success with spot id
        return ParkingValidationStatus.Success(0L)
    }
}
