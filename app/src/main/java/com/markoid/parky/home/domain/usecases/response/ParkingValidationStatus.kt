package com.markoid.parky.home.domain.usecases.response

import android.content.res.Resources
import com.markoid.parky.R

const val EMPTY_STRING = ""

sealed class ParkingValidationStatus(val message: String) {
    object Success : ParkingValidationStatus(EMPTY_STRING)

    sealed class Failure(errorMessage: String) : ParkingValidationStatus(errorMessage) {
        data class EmptyAddress(val res: Resources) :
            Failure(res.getString(R.string.invalid_address_message))

        data class InvalidLocation(val res: Resources) :
            Failure(res.getString(R.string.invalid_location_message))

        data class InvalidParkingTime(val res: Resources) :
            Failure(res.getString(R.string.invalid_parking_time_message))

        data class InvalidParkingType(val res: Resources) :
            Failure(res.getString(R.string.invalid_parking_type_message))

        data class InvalidFloorType(val res: Resources) :
            Failure(res.getString(R.string.invalid_floor_type_message))

        data class EmptyFloorNumber(val res: Resources) :
            Failure(res.getString(R.string.invalid_floor_number_message))

        data class EmptyColor(val res: Resources) :
            Failure(res.getString(R.string.invalid_color_message))

        data class InvalidLotIdentifier(val res: Resources) :
            Failure(res.getString(R.string.invalid_lot_identifier_message))

        data class InvalidFare(val res: Resources) :
            Failure(res.getString(R.string.invalid_fare_message))
    }
}
