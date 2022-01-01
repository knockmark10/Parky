package com.markoid.parky.home.data.mappers

import com.markoid.parky.home.data.entities.ParkingSpotEntity
import com.markoid.parky.home.data.entities.ParkingSpotStatus
import com.markoid.parky.home.data.extensions.toEntity
import com.markoid.parky.home.domain.usecases.request.ParkingSpotRequest
import javax.inject.Inject

class ParkingSpotMapper @Inject constructor() {

    fun mapFromRequestToEntity(
        request: ParkingSpotRequest,
        status: ParkingSpotStatus
    ): ParkingSpotEntity = request.toEntity(status)
}
