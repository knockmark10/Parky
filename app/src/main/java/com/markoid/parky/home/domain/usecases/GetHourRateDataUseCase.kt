package com.markoid.parky.home.domain.usecases

import android.content.res.Resources
import com.markoid.parky.core.domain.usecases.UseCase
import com.markoid.parky.core.presentation.extensions.minutes
import com.markoid.parky.core.presentation.extensions.seconds
import com.markoid.parky.home.data.extensions.latLng
import com.markoid.parky.home.domain.usecases.response.HourRateResponse
import com.markoid.parky.home.presentation.enums.ParkingFloorType
import org.joda.time.DateTime
import org.joda.time.Duration
import javax.inject.Inject

class GetHourRateDataUseCase
@Inject constructor(
    private val getParkingSpotUseCase: GetActiveParkingSpotUseCase,
    private val resources: Resources
) : UseCase<HourRateResponse, Unit>() {

    override suspend fun onExecute(request: Unit): HourRateResponse {
        val spot = getParkingSpotUseCase.onExecute(Unit)
        return HourRateResponse(
            spot.address,
            getFloorLabel(spot.floorNumber, spot.floorType!!),
            getHourRate(spot.hourRate),
            spot.latLng,
            spot.lotIdentifier,
            getParkedTime(spot.parkingTime),
            getTotal(spot.parkingTime, spot.hourRate)
        )
    }

    private fun getFloorLabel(floorNumber: String, floorType: ParkingFloorType): String {
        val floorBuilder = StringBuilder()
        when (floorNumber) {
            "1" -> floorBuilder.append(floorNumber).append("st")
            "2" -> floorBuilder.append(floorNumber).append("nd")
            "3" -> floorBuilder.append(floorNumber).append("rd")
            else -> floorBuilder.append(floorNumber).append("th")
        }
        return floorBuilder.append(" ").append(floorType.getLocalizedValue(resources)).toString()
    }

    private fun getHourRate(rate: Double): String = StringBuilder()
        .append("$")
        .append(String.format("%.2f", rate))
        .append(" /h")
        .toString()

    private fun getParkedTime(parkingTime: DateTime): String {
        val parkingDuration = Duration(parkingTime, DateTime.now())
        return when {
            parkingDuration.standardDays >= 1 -> StringBuilder()
                .append(parkingDuration.standardDays)
                .append(" ")
                .append("d")
                .toString()
            parkingDuration.standardHours >= 1 -> StringBuilder()
                .append(parkingDuration.standardHours)
                .append(" ")
                .append("hrs")
                .toString()
            parkingDuration.standardMinutes >= 1 -> StringBuilder()
                .append(parkingDuration.standardMinutes)
                .append(" ")
                .append("min")
                .toString()
            parkingDuration.standardSeconds >= 1 -> StringBuilder()
                .append(parkingDuration.standardSeconds)
                .append(" ")
                .append("sec")
                .toString()
            else -> "-"
        }
    }

    private fun getTotal(parkingTime: DateTime, hourRate: Double): String {
        // Calculate the parking duration time
        val parkingDuration = Duration(parkingTime, DateTime.now())
        // Initialize our total
        var total = 0.0
        // Check for fifteen minute tolerance
        val isFifteenMinuteToleranceActive = parkingDuration.isShorterThan(15.minutes)
        // Calculate the hour rate for the hours parked
        if (parkingDuration.standardHours >= 1) {
            total = hourRate * parkingDuration.standardHours
            parkingDuration.minus(parkingDuration.standardHours)
        }
        // When tolerance is not active, add the hour rate to the total is there is a second on the clock
        if (isFifteenMinuteToleranceActive.not() && parkingDuration.isLongerThan(1.seconds)) {
            total += hourRate
        }
        // Return formatted hour rate
        return StringBuilder().append("$").append(String.format("%.2f", total)).toString()
    }
}
