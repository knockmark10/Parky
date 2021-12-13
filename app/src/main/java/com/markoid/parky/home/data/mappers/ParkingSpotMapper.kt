package com.markoid.parky.home.data.mappers

import android.content.res.Resources
import com.markoid.parky.home.data.entities.ParkingSpotEntity
import com.markoid.parky.home.data.entities.ParkingSpotStatus
import com.markoid.parky.home.domain.usecases.request.ParkingSpotRequest
import javax.inject.Inject

class ParkingSpotMapper @Inject constructor(private val resources: Resources) {

    fun mapFromRequestToEntity(
        request: ParkingSpotRequest,
        status: ParkingSpotStatus
    ): ParkingSpotEntity = request.toEntity(resources, status)
}
