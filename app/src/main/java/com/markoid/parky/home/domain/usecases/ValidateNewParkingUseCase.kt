package com.markoid.parky.home.domain.usecases

import android.content.res.Resources
import com.markoid.parky.core.domain.usecases.UseCase
import com.markoid.parky.home.domain.usecases.request.ValidateParkingRequest
import com.markoid.parky.home.domain.usecases.response.ParkingValidationStatus
import com.markoid.parky.home.presentation.enums.ParkingFloorType
import com.markoid.parky.home.presentation.enums.ParkingType
import com.markoid.parky.position.presentation.extensions.isValid
import javax.inject.Inject

class ValidateNewParkingUseCase
@Inject constructor(
    private val resources: Resources
) : UseCase<ParkingValidationStatus, ValidateParkingRequest>() {

    override suspend fun onExecute(request: ValidateParkingRequest): ParkingValidationStatus = when {
        request.address.isEmpty() -> ParkingValidationStatus.Failure.EmptyAddress(resources)
        request.latitude.isValid.not() && request.longitude.isValid.not() ->
            ParkingValidationStatus.Failure.InvalidLocation(resources)
        request.parkingTime.isEmpty() -> ParkingValidationStatus.Failure.InvalidParkingTime(resources)
        ParkingType.exists(resources, request.parkingType).not() ->
            ParkingValidationStatus.Failure.InvalidParkingType(resources)
        else -> validateParkingLotInformation(request)
    }

    private fun validateParkingLotInformation(request: ValidateParkingRequest): ParkingValidationStatus {
        // Get ParkingType from previous validated raw value. Null assertion is valid here.
        val parkingType = ParkingType.forValue(resources, request.parkingType)!!
        return if (parkingType == ParkingType.ParkingLot) {
            when {
                ParkingFloorType.exists(resources, request.floorType).not() ->
                    ParkingValidationStatus.Failure.InvalidFloorType(resources)

                request.floorNumber.isEmpty() ->
                    ParkingValidationStatus.Failure.EmptyFloorNumber(resources)

                request.color.isEmpty() -> ParkingValidationStatus.Failure.EmptyColor(resources)

                request.lotIdentifier.isEmpty() ->
                    ParkingValidationStatus.Failure.InvalidLotIdentifier(resources)

                request.fare < 0.0 -> ParkingValidationStatus.Failure.InvalidFare(resources)

                else -> ParkingValidationStatus.Success
            }
        } else {
            ParkingValidationStatus.Success
        }
    }
}
