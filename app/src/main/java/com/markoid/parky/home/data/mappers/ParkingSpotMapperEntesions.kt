package com.markoid.parky.home.data.mappers

import android.content.res.Resources
import com.markoid.parky.home.data.entities.ParkingSpotEntity
import com.markoid.parky.home.data.entities.ParkingSpotStatus
import com.markoid.parky.home.domain.usecases.request.ParkingSpotRequest
import com.markoid.parky.home.presentation.enums.ParkingFloorType
import com.markoid.parky.home.presentation.enums.ParkingType

fun ParkingSpotRequest.toEntity(
    res: Resources,
    status: ParkingSpotStatus
): ParkingSpotEntity = ParkingSpotEntity(
    address = address,
    alarmTime = alarmTime,
    color = color,
    hourRate = hourRate,
    floorNumber = floorNumber,
    floorType = ParkingFloorType.forValue(res, floorType),
    id = id ?: 0L,
    latitude = latitude,
    longitude = longitude,
    lotIdentifier = lotIdentifier,
    parkingTime = parkingTime,
    parkingType = ParkingType.forValue(res, parkingType)
        ?: throw IllegalStateException("Parking type was not found for value: $parkingType"),
    photo = photo,
    status = status
)
