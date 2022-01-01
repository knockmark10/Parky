package com.markoid.parky.home.data.extensions

import com.markoid.parky.home.data.entities.ParkingSpotEntity
import com.markoid.parky.home.data.entities.ParkingSpotStatus
import com.markoid.parky.home.domain.usecases.request.ParkingSpotRequest
import com.markoid.parky.home.presentation.enums.ParkingColor
import com.markoid.parky.home.presentation.enums.ParkingFloorType
import com.markoid.parky.home.presentation.enums.ParkingType

fun ParkingSpotRequest.toEntity(
    status: ParkingSpotStatus
): ParkingSpotEntity = ParkingSpotEntity(
    address = address,
    alarmTime = alarmTime,
    color = ParkingColor.forValue(color),
    hourRate = hourRate,
    floorNumber = floorNumber,
    floorType = ParkingFloorType.forValue(floorType),
    id = id ?: 0L,
    latitude = latitude,
    longitude = longitude,
    lotIdentifier = lotIdentifier,
    parkingTime = parkingTime,
    parkingType = ParkingType.forValue(parkingType)
        ?: throw IllegalStateException("Parking type was not found for value: $parkingType"),
    photo = photo,
    status = status
)
